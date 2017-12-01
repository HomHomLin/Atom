package com.meiyou.atom.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Linhh on 2017/11/30.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface SupressCode {
    String value() default "";
}