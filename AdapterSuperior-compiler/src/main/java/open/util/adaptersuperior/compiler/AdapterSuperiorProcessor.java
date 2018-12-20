package open.util.adaptersuperior.compiler;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import open.util.adaptersuperior.annotation.AdapterHolder;
import open.util.adaptersuperior.annotation.InjectAdapter;
import open.util.adaptersuperior.annotation.UndefinedAdapter;

/**
 * Created by SAME.LI on 2018/12/17
 */
@AutoService(Processor.class)
public class AdapterSuperiorProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
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
        printInfo("process start");
        Set<? extends Element> rootElements = roundEnvironment.getRootElements();
        for (Element element : rootElements) {
            processElement(element);
        }
        return false;
    }

    //处理所有含注解class
    private void processElement(Element element) {
        final String packagename = ProcessorUtil.getClassPackage(mElementUtils, element);
        final String hostName = ProcessorUtil.getClassSimpleName(element);

        List<? extends Element> allMembers = mElementUtils.getAllMembers((TypeElement) element);
        if (null != allMembers) {
            // 遍历类的成员属性
            for (Element member : allMembers) {

                InjectAdapter adapterHolder = member.getAnnotation(InjectAdapter.class);
                if (null != adapterHolder) {
                    final String fieldName = ProcessorUtil.getMemberFieldName(member);
                    printInfo("fieldName=%s", fieldName);
                    String adapterName = ProcessorUtil.obtainClassName(fieldName);
                    final String newAdapterClassName = hostName + "$$" + adapterName;
                    printInfo("newAdapterClassName=%s", newAdapterClassName);
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


                        MethodSpec.Builder onCreateViewHolder = onCreateViewHolder(viewHolderClassName);
                        addCodeForOnCreateViewHolderAndOnBindViewHolder(onCreateViewHolder,onBindViewHolder,member, adapterHolder );
                        apdaterTypeSpec.addMethod(onBindViewHolder.build());
                        apdaterTypeSpec.addMethod(onCreateViewHolder.build());
                        apdaterTypeSpec.addMethod(getItemType().build());
                        JavaFile.builder(packagename
                                , apdaterTypeSpec.build())
                                .build().writeTo(mFiler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }


    }



    private MethodSpec.Builder getItemType(){

        return MethodSpec.methodBuilder("getItemViewType")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(int.class,
                        "position", Modifier.FINAL)
                .returns(TypeName.INT)
                .addStatement("java.lang.Class<?> modelClass = this.getItemData(position).getClass()")
                .addStatement("$L adapterModelAnn = modelClass.getAnnotation($L.class) " , ClassNames.ADAPTER_MODEL, ClassNames.ADAPTER_MODEL )
                .beginControlFlow("if(null != adapterModelAnn)")
                .addStatement("java.lang.String idResName =  adapterModelAnn.$L()", ClassNames.ADAPTER_MODEL_VIEWTYPE_IDRESNAME)
                .addStatement("return null != idResName && !idResName.isEmpty()?$L.$L(idResName, \"id\"):adapterModelAnn.$L()", ClassNames.APP_UTIL, ClassNames.APP_UTIL__GET_IDENTIFIER ,ClassNames.ADAPTER_MODEL_TYPE )
                .endControlFlow("return super.getItemViewType(position)");
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

    //完善 OnCreateViewHolder OnBindViewHolder 方法
    private   void addCodeForOnCreateViewHolderAndOnBindViewHolder(MethodSpec.Builder onCreateViewHolder, MethodSpec.Builder onBindViewHolder,  Element member,  InjectAdapter adapterHolder){
        String[] holders = null;
        try {
            Class<?>[] adapterHolderClass = adapterHolder.value();
            if(0 != adapterHolderClass.length){
                holders =  ProcessorUtil.getClassNameArray(adapterHolderClass);
            }
        } catch (MirroredTypesException mte) {
            holders = ProcessorUtil.getClassNameArray(mte);
        }

        if(  null == holders || 0 == holders.length){
            onCreateViewHolder.addStatement(" return null");
            return;
        }
        for (int i = 0; i < holders.length; i++) {
            String holder = holders[i];
            TypeElement layoutElement = mElementUtils.getTypeElement(holder);
            if (null != layoutElement) {
                AdapterHolder annotation = layoutElement.getAnnotation(AdapterHolder.class);

                String modelAdapterClassName ;

                try {
                    Class<?> model = annotation.model();
                    modelAdapterClassName = model.getName();
                } catch (MirroredTypeException  e) {
                    modelAdapterClassName = ProcessorUtil.getClass(e);
                }


                String viewType = "0";
                if (i == 0) {
                    onCreateViewHolder.beginControlFlow("if($L == viewType)", viewType);

                    onBindViewHolder.beginControlFlow("if (holder instanceof $L)", holder);
                } else {
                    onCreateViewHolder.beginControlFlow("else if($L == viewType)", viewType);

                    onBindViewHolder.beginControlFlow("else if (holder instanceof $L)", holder);
                }

                onCreateViewHolder.addStatement("final android.view.View layoutView = android.view.LayoutInflater.from(parent.getContext()).inflate($L, parent, false)", writeCallGetIdMethod(annotation.layoutResName(), "layout"));
                onCreateViewHolder.endControlFlow();

                onBindViewHolder.addStatement("final $L tHolder = ($L)holder ", holder, holder);
                onBindViewHolder.addStatement("final java.util.List<$L> list =  getDataSet()", modelAdapterClassName);
                onBindViewHolder.addStatement("$L data = null" ,modelAdapterClassName);
                onBindViewHolder.beginControlFlow("if(null !=  list && !list.isEmpty())");
                onBindViewHolder.addStatement("data = list.get(position)");
                onBindViewHolder.endControlFlow();
                onBindViewHolder.addStatement("tHolder.update(position, data, list)");
                onBindViewHolder.endControlFlow();
            }
        }
        onCreateViewHolder.addStatement("return  new $L(new $L(parent.getContext()))", ClassNames.EMPTYADAPTERHOLDER,ClassNames.VIEW);
    }

    public String writeCallGetIdMethod(String id, String defType){
        return ClassNames.APP_UTIL+"."+ClassNames.APP_UTIL__GET_IDENTIFIER+"(\""+id+"\",\""+defType+"\")";
    }

    private void printInfo(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, String.format(msg, args));
    }

    private void printERROR(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

}
