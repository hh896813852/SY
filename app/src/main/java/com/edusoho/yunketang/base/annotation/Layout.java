package com.edusoho.yunketang.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
    int value() default -1;              // 资源id
    String title() default "未设置标题"; // 标题
    String rightButton() default "未知"; // 右侧文字标题
    int rightButtonRes() default -1;     // 右边图标
}
