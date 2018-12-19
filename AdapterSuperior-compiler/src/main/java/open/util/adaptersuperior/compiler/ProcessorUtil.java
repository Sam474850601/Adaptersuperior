package open.util.adaptersuperior.compiler;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import open.util.adaptersuperior.annotation.UndefinedAdapter;

/**
 * Created by SAME.LI on 2018/12/17
 */
public class ProcessorUtil {


    public static String getClassPackage(Elements elements, Element element) {
        return elements.getPackageOf(element).getQualifiedName().toString();
    }


    public static String getClassSimpleName(Element element) {
        return element.getSimpleName().toString();
    }

    public static String getMemberFieldName(Element member) {
        return member.getSimpleName().toString();
    }


    public static String[] getClassPackageAndSimpleName(String className) {
        final int lastpackagePoint = className.lastIndexOf(".");
        return new String[]{className.substring(0, lastpackagePoint), className.substring(lastpackagePoint + 1, className.length())};
    }

    //create a class name with a value that was upperCase its first chard
    public static String obtainClassName(String value) {
        String adapterName = value.substring(0, 1).toUpperCase();
        if (value.length() > 1) {
            adapterName += value.substring(1, value.length());
        }
        return adapterName;
    }


    public static String getClass(MirroredTypeException mte) {
        TypeMirror typeMirror = mte.getTypeMirror();
        DeclaredType classTypeMirror = (DeclaredType) typeMirror;
        TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
        return classTypeElement.getQualifiedName().toString();
    }

    public static String[] getClassNameArray(MirroredTypesException mte) {
        String[] holders = null;
        List<? extends TypeMirror> typeArguments = mte.getTypeMirrors();
        if (null == typeArguments || typeArguments.isEmpty())
            return null;
        final int size = typeArguments.size();
        holders = new String[size];
        for (int i = 0; i < size; i++) {
            DeclaredType classTypeMirror = (DeclaredType) typeArguments.get(i);
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            holders[i] = classTypeElement.getQualifiedName().toString().toString();
        }
        return holders;
    }

    public static String[] getClassNameArray(Class<?> ...classArray) {
        String[] holders = new String[classArray.length];
        for (int i = 0; i < classArray.length; i++) {
            holders[i] = classArray[i].getName();
        }
        return holders;
    }



}
