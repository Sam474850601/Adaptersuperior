package com.to8to.app.adaptersuperior.annotation;

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
      Class<?> model() default UndefinedAdapterModel.class ;
}
