package com.to8to.app.adaptersuperior.compiler;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import com.to8to.app.adaptersuperior.annotation.AdapterHolder;
import com.to8to.app.adaptersuperior.annotation.AdapterModel;
import com.to8to.app.adaptersuperior.annotation.InjectAdapter;
import com.to8to.app.adaptersuperior.annotation.UndefinedAdapter;

/**
 * Created by SAME.LI on 2018/12/17
 */
@AutoService(Processor.class)
public class AdapterSuperiorProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    long startTime;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        startTime = System.currentTimeMillis();
        printInfo("start scan ");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(AdapterHolder.class.getCanonicalName());
        types.add(InjectAdapter.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> rootElements = roundEnvironment.getRootElements();
        for (Element element : rootElements) {
            processElement(element);
        }
        printInfo("from start to now what cost time is %s seconds ",(System.currentTimeMillis()-startTime)/1000f );
        return false;
    }

    //class proccess all class that was inject annotation defined
    private void processElement(Element element) {
        final String packagename = ProcessorUtil.getClassPackage(mElementUtils, element);
        final String hostName = ProcessorUtil.getClassSimpleName(element);
        final HashMap<String, ClassName> adapterClassNames = new HashMap<>();
        List<? extends Element> allMembers = mElementUtils.getAllMembers((TypeElement) element);
        if (null != allMembers) {
            // search menber variable and create class
            for (Element member : allMembers) {

                InjectAdapter adapterHolder = member.getAnnotation(InjectAdapter.class);
                if (null != adapterHolder) {
                    final String fieldName = ProcessorUtil.getMemberFieldName(member);
                    String adapterName = ProcessorUtil.obtainClassName(fieldName);
                    final String newAdapterClassName = hostName + "$$" + adapterName;
                    TypeSpec.Builder apdaterTypeSpec = TypeSpec.classBuilder(newAdapterClassName);
                    try {
                        String parent = null;
                        try {
                            Class value = adapterHolder.parent();
                            if (!UndefinedAdapter.class.isAssignableFrom(value)) {
                                parent = value.getName();
                            }
                        } catch (MirroredTypeException mte) {
                            parent = ProcessorUtil.getClass(mte);
                            if (UndefinedAdapter.class.getName().equals(parent)) {
                                parent = null;
                            }
                        }
                        if (null == parent || parent.isEmpty()) {
                            parent = ClassNames.ADAPTER_SUPPORTDEFAULT;
                        }
                        String[] packagAnName = ProcessorUtil.getClassPackageAndSimpleName(parent);

                        apdaterTypeSpec.addModifiers(Modifier.PUBLIC)
                                .superclass(ClassName.get(packagAnName[0], packagAnName[1]));

                        ClassName viewHolderClassName = ClassName.get(ClassNames.RECYCLERVIEW, ClassNames.VIEWHOLDER);
                        MethodSpec.Builder onBindViewHolder = onBindViewHolder(viewHolderClassName);


                        MethodSpec.Builder itemType =  getItemViewType();
                        MethodSpec.Builder onCreateViewHolder = onCreateViewHolder(viewHolderClassName);
                        fildAllMethodCodes(onCreateViewHolder, onBindViewHolder, itemType, adapterHolder);
                        apdaterTypeSpec.addMethod(onBindViewHolder.build());
                        apdaterTypeSpec.addMethod(onCreateViewHolder.build());
                        apdaterTypeSpec.addMethod(itemType.build());
                        JavaFile.builder(packagename
                                , apdaterTypeSpec.build())
                                .build().writeTo(mFiler);

                        adapterClassNames.put(fieldName, ClassName.get(packagename, newAdapterClassName) );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        if(!adapterClassNames.isEmpty()){

            MethodSpec.Builder builder = MethodSpec.methodBuilder(ClassNames.INJECT_HANDLER_INIT);
            builder.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.STATIC).returns(TypeName.VOID);
            builder .addParameter(ClassName.get(packagename,hostName), "container", Modifier.FINAL);

            Set<Map.Entry<String, ClassName>> entries = adapterClassNames.entrySet();
            int i = 0;
            for (Map.Entry<String, ClassName> entry : entries) {
                if(0 == i){
                    printInfo("start to create adapter class:",packagename+"."+hostName);
                    i++;
                }

                ClassName adapterClassName = entry.getValue();//
                printInfo("inject adapter: %s",adapterClassName.packageName()+"."+adapterClassName.simpleName());
                String fieldName = entry.getKey();//the field name of container class
                builder.addStatement("container.$L= new $L()", fieldName , adapterClassName.packageName()+"."+adapterClassName.simpleName());
            }

            final String newHandlerClassName = hostName + "$$" + ClassNames.INJECT_HANDLER;
            TypeSpec.Builder apdaterTypeSpec = TypeSpec.classBuilder(newHandlerClassName);
            apdaterTypeSpec.addMethod(builder.build());
            try {
                JavaFile.builder(packagename
                        , apdaterTypeSpec.build())
                        .build()
                        .writeTo(mFiler);
                printInfo("create init class  %s",packagename+"."+newHandlerClassName);
            } catch (Exception e) {
                printInfo("create adapter error: %s", e.getMessage());
            }
        }


    }


    private MethodSpec.Builder getItemViewType() {
        return MethodSpec.methodBuilder("getItemViewType")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(int.class,
                        "position", Modifier.FINAL)
                .returns(TypeName.INT);
    }


    private MethodSpec.Builder onBindViewHolder(ClassName viewHolderClassName) {
        return MethodSpec.methodBuilder("onBindViewHolder")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(viewHolderClassName,
                        "holder")
                .addParameter(int.class,
                        "position", Modifier.FINAL)
                .returns(TypeName.VOID);
    }

    private MethodSpec.Builder onCreateViewHolder(ClassName viewHolderClassName) {
        //    @Overridepublic RecyclerView.ViewHolder onCreateViewHolder
        //    (ViewGroup parent, int viewType) {return null;}
        return
                MethodSpec.methodBuilder("onCreateViewHolder")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(ClassName.get(ClassNames.PACKAGE_VIEW,
                                "ViewGroup"),
                                "parent")
                        .addParameter(int.class,
                                "viewType", Modifier.FINAL)
                        .returns(viewHolderClassName);
    }

    //fill  OnCreateViewHolder OnBindViewHolder method
    private void fildAllMethodCodes(MethodSpec.Builder onCreateViewHolder, MethodSpec.Builder onBindViewHolder,  MethodSpec.Builder itemType,InjectAdapter adapterHolder) {



        String[] holders = null;
        try {
            Class<?>[] adapterHolderClass = adapterHolder.value();
            if (0 != adapterHolderClass.length) {
                holders = ProcessorUtil.getClassNameArray(adapterHolderClass);
            }
        } catch (MirroredTypesException mte) {
            holders = ProcessorUtil.getClassNameArray(mte);
        }

        if (null == holders || 0 == holders.length) {
            onCreateViewHolder.addStatement("return  new $L(new $L(parent.getContext()))", ClassNames.EMPTYADAPTERHOLDER, ClassNames.VIEW);
            return;
        }
        itemType.addStatement("final Object data = this.getItemData(position)");




        for (int i = 0; i < holders.length; i++) {
            String holder = holders[i];
            TypeElement layoutElement = mElementUtils.getTypeElement(holder);
            if (null != layoutElement) {
                AdapterHolder annotation = layoutElement.getAnnotation(AdapterHolder.class);
                if(null == annotation)
                    continue;
                String modelAdapterClassName;

                try {
                    Class<?> model = annotation.model();
                    modelAdapterClassName = model.getName();
                } catch (MirroredTypeException e) {
                    modelAdapterClassName = ProcessorUtil.getClass(e);
                }

                TypeElement typeElement = null ;
                try {
                    typeElement =   mElementUtils.getTypeElement(modelAdapterClassName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AdapterModel adapterModel = null;
                if(null != typeElement){
                    adapterModel =  typeElement.getAnnotation(AdapterModel.class);
                    if(null != adapterModel){
                        itemType.addStatement("if( data instanceof $L) return $L",modelAdapterClassName,  getViewTypeId(adapterModel) );
                    }else {
                        printWarning("%s had not injected AdapterModel", modelAdapterClassName );
                        itemType.addStatement("if( data instanceof $L) return $L",modelAdapterClassName,  0);
                    }
                }


                String viewType = null != adapterModel ?  getViewTypeId(adapterModel) :"0";
                if (0 == i) {
                    onCreateViewHolder.beginControlFlow("if($L == viewType)", viewType);
                    onBindViewHolder.beginControlFlow("if (holder instanceof $L)", holder);
                } else {
                    onCreateViewHolder.beginControlFlow("else if($L == viewType)", viewType);
                    onBindViewHolder.beginControlFlow("else if (holder instanceof $L)", holder);
                }
                onCreateViewHolder.addStatement("return  new $L(android.view.LayoutInflater.from(parent.getContext()).inflate($L, parent, false))", holder, writeCallGetIdMethod(annotation.layoutResName(), "layout"));
                onCreateViewHolder.endControlFlow();
                onBindViewHolder.addStatement("final $L tHolder = ($L)holder ", holder, holder);
                onBindViewHolder.addStatement("final java.util.List<$L> list =  getDataSet()", modelAdapterClassName);
                onBindViewHolder.addStatement("$L data = null", modelAdapterClassName);
                onBindViewHolder.beginControlFlow("if(null !=  list && !list.isEmpty() &&position>=0 && position<list.size())");
                onBindViewHolder.addStatement("data = list.get(position)");
                onBindViewHolder.endControlFlow();
                onBindViewHolder.addStatement("tHolder.update(position, data, list)");
                onBindViewHolder.endControlFlow();



            }
        }

        onCreateViewHolder.addStatement("return  new $L(new $L(parent.getContext()))", ClassNames.EMPTYADAPTERHOLDER, ClassNames.VIEW);
        itemType.addStatement("return super.getItemViewType(position)");



    }

    private String getViewTypeId(AdapterModel adapterModel){
        String viewTypeIdResName = adapterModel.viewTypeIdResName();
        return !viewTypeIdResName.isEmpty()?writeCallGetIdMethod(viewTypeIdResName, "id"):adapterModel.viewType()+"";
    }

    public String writeCallGetViewType(String name) {
        return ClassNames.APP_UTIL + "." + ClassNames.APP_UTIL__GET_ADAPTER_VIEWTYPEID + "(" + name + ")";
    }


    public String writeCallGetIdMethod(String id, String defType) {
        return ClassNames.APP_UTIL + "." + ClassNames.APP_UTIL__GET_IDENTIFIER + "(\"" + id + "\",\"" + defType + "\")";
    }


    public String writeCallAdapterModelAnnotation(String className) {
        return ClassNames.APP_UTIL + "." + ClassNames.APP_UTIL__GET_ADAPTERMODELANNOTATION + "(" + className + ")";
    }


    public String writeCallGetViewTypeIdByClassName(String className) {
        return ClassNames.APP_UTIL + "." + ClassNames.APP_UTIL__GET_VIEWTYPEBYCLASS + "(" + className + ".class)";
    }


    static final String TAG = "AdapterSuperior:===>   ";

    private void printInfo(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.OTHER, String.format(TAG+msg, args));
    }


    private void printWarning(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, String.format(TAG+msg, args));
    }

    private void printERROR(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(TAG+msg, args));
    }

}
