package com.meiyou.atom.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Linhh on 2017/10/11.
 */

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface UiThread {
    //需要一些额外参数
    //需要一些额外参数
    String taskName() default "";
    long delay() default 0;
    String mode() default "";
}
