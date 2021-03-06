package com.to8to.app.adaptersuperior.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by same.li on 2018/12/18
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AdapterModel {
    int  viewType() default 0;
    String  viewTypeIdResName() default "";
}
