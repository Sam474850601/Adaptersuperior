package com.to8to.app.adaptersuperior.lib;

import android.app.Application;

import com.to8to.app.adaptersuperior.annotation.AdapterModel;

import java.lang.reflect.Method;


/**
 * Created by same.li on 2018/12/18
 */
public class AdapterSuperiorAppUtil {
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


    public static int getViewResId(String id){
        return getApplicationContext().getResources().getIdentifier(id, "id", getPackageName());
    }


    public static int getLayoutResId(String id){
        return getApplicationContext().getResources().getIdentifier(id, "layout", getPackageName());
    }




    //获取viewTypeId
    public static int getAdapterModelViewTypeId(AdapterModel adapterModelAnn) {
        String idResName = adapterModelAnn.viewTypeIdResName();
        return !idResName.isEmpty() ? AdapterSuperiorAppUtil.getIdentifier(idResName, "id") : adapterModelAnn.viewType();
    }


}
