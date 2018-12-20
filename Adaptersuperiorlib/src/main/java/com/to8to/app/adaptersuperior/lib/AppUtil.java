package com.to8to.app.adaptersuperior.lib;

import android.app.Application;

import com.to8to.app.adaptersuperior.annotation.AdapterModel;

import java.lang.reflect.Method;


/**
 * Created by same.li on 2018/12/18
 */
public class AppUtil {
    private volatile static Application application;
    private static String packageName;

    private static Application getApplicationContext() {
        if (null == application) {
            try {
                Class<?> activityThread = Class.forName("android.app.ActivityThread");
                Method currentActivityThreadMethod = activityThread.getMethod("currentActivityThread");
                Object currentActivityThreadObject = currentActivityThreadMethod.invoke((Object) null);
                Method getApplicationMethod = activityThread.getMethod("getApplication");
                Object app = getApplicationMethod.invoke(currentActivityThreadObject);
                return application = (Application) app;
            } catch (Exception ex) {
            }
        }
        return application;
    }


    public static String getPackageName() {
        if (null == packageName) {
            return packageName = getApplicationContext().getPackageName();
        }
        return packageName;
    }

    public static int getIdentifier(String id, String defType) {
        return getApplicationContext().getResources().getIdentifier(id, defType, getPackageName());
    }



    //根据被AdapterModel注解的类获取AdapterModel
    public AdapterModel getAdapterModelAnnotation(String className){
        try {
            Class<?> aClass = Class.forName(className);
            AdapterModel annotation = aClass.getAnnotation(AdapterModel.class);
            return annotation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }



    //获取viewTypeId
    public static int getAdapterModelViewTypeId(AdapterModel adapterModelAnn) {
        String idResName = adapterModelAnn.viewTypeIdResName();
        return !idResName.isEmpty() ? AppUtil.getIdentifier(idResName, "id") : adapterModelAnn.viewType();
    }

    public static int getViewTypeByClassName(Class<?>  adapterModeClass){
        AdapterModel adapterModelAnnotation = adapterModeClass.getAnnotation(AdapterModel.class);
        if(null != adapterModelAnnotation){
            return  getAdapterModelViewTypeId(adapterModelAnnotation);
        }
        return  0;
    }
}
