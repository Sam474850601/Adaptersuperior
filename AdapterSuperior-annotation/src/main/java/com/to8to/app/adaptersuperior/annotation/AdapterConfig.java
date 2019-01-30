package com.to8to.app.adaptersuperior.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by same.li on 2018/12/24
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AdapterConfig {
    boolean overrideGetItemViewType() default true; //arrow if its child override getItemViewType
    boolean overrideOnCreateViewHolder() default true;//arrow if its child override onCreateViewHolder
    boolean overrideOnBindViewHolder() default true;//arrow if its child override onBindViewHolder
}
