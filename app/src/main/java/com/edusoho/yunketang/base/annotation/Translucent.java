package com.edusoho.yunketang.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by any on 16/7/25.
 * 透明状态栏
 * 增加此注解会在activity调用super.onCreate()的时候
 * 调用StatusBarUtil.setTranslucent()
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Translucent {
    boolean value() default true;
}
