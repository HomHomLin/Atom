package com.meiyou.atom.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface WorkThread {
  //需要一些额外参数
  //需要一些额外参数
  String taskName() default "";
  long delay() default 0;
}
