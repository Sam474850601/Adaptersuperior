package open.util.adaptersuperior.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SAME.LI on 2018/12/17
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AdapterHolder {
      String layoutResName ();
      int viewType() default  0;
      String viewTypeIdResName() default "";
}
