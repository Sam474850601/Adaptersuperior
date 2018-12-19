package open.util.adaptersuperior.compiler;

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
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
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

    long startTime  = 0;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        printInfo("��ʼɨ��ע��������====================��");
        startTime = System.currentTimeMillis();
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

    int i  = 0;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> rootElements = roundEnvironment.getRootElements();
        for (Element element : rootElements) {
            processElement(element);
        }
        printInfo("��ֹĿǰ��ʱ:%f, �����%d��ɨ��", (System.currentTimeMillis()-startTime)/1000f, ++i);
        return false;
    }



    //�������к�ע��class
    private void processElement(Element element) {
        final String packagename = ProcessorUtil.getClassPackage(mElementUtils, element);
        final String hostName = ProcessorUtil.getClassSimpleName(element);

        final List<? extends Element> allMembers = mElementUtils.getAllMembers((TypeElement) element);

        //��ʱ�洢��Ҫ����ʵ��������������
        final HashMap<String, ClassName> adapterClassNames = new HashMap<>();
        //��ʶ�Ƿ���������ע���
        if (null != allMembers) {
            // ������ĳ�Ա����
            for (Element member : allMembers) {
                InjectAdapter adapterHolder = member.getAnnotation(InjectAdapter.class);
                if (null != adapterHolder) {
                    final String fieldName = ProcessorUtil.getMemberFieldName(member);
                    String adapterName = ProcessorUtil.obtainClassName(fieldName);
                    //����������������
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

                        final ClassName viewHolderClassName = ClassName.get(ClassNames.RECYCLERVIEW, ClassNames.VIEWHOLDER);
                        final MethodSpec.Builder onBindViewHolder = onBindViewHolder(viewHolderClassName);


                        final MethodSpec.Builder onCreateViewHolder = onCreateViewHolder(viewHolderClassName);
                        addCodeForOnCreateViewHolderAndOnBindViewHolder(onCreateViewHolder,onBindViewHolder,member, adapterHolder );
                        apdaterTypeSpec.addMethod(onBindViewHolder.build());
                        apdaterTypeSpec.addMethod(onCreateViewHolder.build());
                        apdaterTypeSpec.addMethod(getItemType().build());


                        //����������
                        JavaFile.builder(packagename
                                , apdaterTypeSpec.build())
                                .build().writeTo(mFiler);
                        printInfo("����:%s����%s��Ҫע��", packagename+"."+hostName,fieldName);
                        adapterClassNames.put(fieldName, ClassName.get(packagename, newAdapterClassName) );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }



            //������������������������ô��ȥ���ɴ������÷���
            if(!adapterClassNames.isEmpty()){

                MethodSpec.Builder builder = MethodSpec.methodBuilder(ClassNames.INJECT_HANDLER_INIT);
                builder.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.STATIC).returns(TypeName.VOID);
                builder .addParameter(ClassName.get(packagename,hostName), "container", Modifier.FINAL);

                Set<Map.Entry<String, ClassName>> entries = adapterClassNames.entrySet();
                int i = 0;
                for (Map.Entry<String, ClassName> entry : entries) {
                    if(0 == i){
                        printInfo("��ʼ����%s������������ע�뷽��",packagename+"."+hostName);
                        i++;
                    }

                    ClassName adapterClassName = entry.getValue();//������class
                    printInfo("������������%s",adapterClassName.packageName()+"."+adapterClassName.simpleName());
                    String fieldName = entry.getKey();//�����ֶ���
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
                    printInfo("���ɳ�ʼ�������ࣺ%s",packagename+"."+newHandlerClassName);
                } catch (Exception e) {
                    printInfo("����������ʵ��������ʧ�ܣ�ԭ��:"+e.getMessage());
                }
            }
        }
    }



    private MethodSpec.Builder getItemType(){
        final String modelClass = "modelClass";
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getItemViewType")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(int.class,
                        "position", Modifier.FINAL)
                .returns(TypeName.INT)
                .addStatement("java.lang.Class<?>  $L = this.getItemData(position).getClass()", modelClass);
        final String adapterModelAnn = "adapterModelAnn";
        builder.addStatement("$L $L =  $L.getAnnotation($L.class) " , adapterModelAnn, ClassNames.ADAPTER_MODEL, modelClass, ClassNames.ADAPTER_MODEL );
        builder .beginControlFlow("if(null != adapterModelAnn)");
        builder.addStatement("return $L", writeCallGetAdapterModelViewTypeId(adapterModelAnn));
        builder.endControlFlow();
        builder.endControlFlow("return super.getItemViewType(position)");
        return builder;
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

    //���� OnCreateViewHolder OnBindViewHolder ����
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
        final String adapterModelViewType = "adapterModelViewType";
        final String adapterModelClass = "adapterModelClass";

        onCreateViewHolder.addStatement("int $L = 0", adapterModelViewType);
        onCreateViewHolder.addStatement("java.lang.Class<?> $L = null", adapterModelClass);
        for (int i = 0; i < holders.length; i++) {
            String holder = holders[i];
            TypeElement layoutElement = mElementUtils.getTypeElement(holder);
            if (null != layoutElement) {
                AdapterHolder annotation = layoutElement.getAnnotation(AdapterHolder.class);
                String modeClassName;
                try {
                    Class<?> model = annotation.model();
                    modeClassName = model.getName();
                } catch (MirroredTypeException e) {
                    modeClassName = ProcessorUtil.getClass(e);
                }






                if (i == 0) {

                    onCreateViewHolder.beginControlFlow("if($L == viewType)", adapterModelViewType);

                    onBindViewHolder.beginControlFlow("if (holder instanceof $L)", holder);
                } else {

                    onCreateViewHolder.beginControlFlow("else if($L == viewType)", adapterModelViewType);

                    onBindViewHolder.beginControlFlow("else if (holder instanceof $L)", holder);
                }

                onCreateViewHolder.addStatement("final android.view.View layoutView = android.view.LayoutInflater.from(parent.getContext()).inflate($L, parent, false)", writeCallGetIdMethod(annotation.layoutResName(), "layout"));

                onCreateViewHolder.addStatement("final $L holder =new $L(layoutView)", holder, holder);

                onCreateViewHolder.addStatement("return holder");

                onCreateViewHolder.endControlFlow();

                onBindViewHolder.addStatement("final $L tHolder = ($L)holder ", holder, holder);
                onBindViewHolder.addStatement("final java.util.List list =  getDataSet()");
                onBindViewHolder.addStatement("java.lang.Object data  = null");
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

    public String writeCallGetAdapterModelViewTypeId(String variable){
        return ClassNames.APP_UTIL+"."+ClassNames.APP_UTIL__GET_ADAPTERMODELID+"("+variable+")";
    }

    static final String TAG = "����������:";

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
