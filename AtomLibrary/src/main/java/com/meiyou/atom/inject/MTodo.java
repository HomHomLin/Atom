package com.meiyou.atom.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Linhh on 2017/12/4.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface MTodo {
    String msg() default "";
    boolean expried() default true;
}