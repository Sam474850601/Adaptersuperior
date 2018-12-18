package open.util.adaptersuperiorlib;

import android.app.Application;

import java.lang.reflect.Method;


/**
 * Created by same.li on 2018/12/18
 */
public class AppUtil {
    private volatile static   Application application;
    private static String packageName;
    private static  Application getApplicationContext() {
        if(null == application){
            try {
                Class<?> activityThread = Class.forName("android.app.ActivityThread");
                Method currentActivityThreadMethod = activityThread.getMethod("currentActivityThread");
                Object currentActivityThreadObject = currentActivityThreadMethod.invoke((Object) null);
                Method getApplicationMethod = activityThread.getMethod("getApplication");
                Object app = getApplicationMethod.invoke(currentActivityThreadObject);
                return application =  (Application) app;
            } catch (Exception ex) {
            }
        }
        return application;
    }

    public static String getPackageName(){
        if(null == packageName){
            return packageName = getApplicationContext().getPackageName();
        }
        return packageName;
    }

    public static int getIdentifier(String id , String defType){
        return getApplicationContext().getResources().getIdentifier(id, defType, getPackageName());
    }

}
