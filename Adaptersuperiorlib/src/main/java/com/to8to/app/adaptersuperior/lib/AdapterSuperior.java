package com.to8to.app.adaptersuperior.lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by same.li on 2018/12/19
 */
public class AdapterSuperior {

    public static final String INJECT_CLASS_SIMPLE_NAME = "AdapterSuperiorHandler";
    public static final String INJECT_CLASS_INJECT_METHOD= "init";


    //给容器被注解属性注入实例
    public static void inject(Object container)
    {
        try {
            invoke(container, INJECT_CLASS_INJECT_METHOD);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static Object invoke(Object object, String methodName) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> hostClass = object.getClass();
        String helperClassName = hostClass.getName() + "$$"
                + INJECT_CLASS_SIMPLE_NAME;
        Class<?> helperClass = Class.forName(helperClassName);
        Method method = helperClass.getMethod(methodName, new Class[]{hostClass});
        return method.invoke(null, object);
    }

}
