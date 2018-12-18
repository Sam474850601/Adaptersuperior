package open.util.adaptersuperior.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SAME.LI on 2018/12/17
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface  InjectAdapter {
    Class<? extends IAdapterHolder>[] value();
    Class<? extends IAdapter>  parent() default UndefinedAdapter.class;
}
