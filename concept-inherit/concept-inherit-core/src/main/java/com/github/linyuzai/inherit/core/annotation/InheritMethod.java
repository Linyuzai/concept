package com.github.linyuzai.inherit.core.annotation;

import com.github.linyuzai.inherit.core.flag.InheritFlag;

import java.lang.annotation.*;

@Repeatable(InheritMethods.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritMethod {

    /**
     * 继承属性的源 Class
     */
    Class<?>[] sources();

    /**
     * 是否继承父类属性
     */
    boolean inheritSuper() default false;

    /**
     * 排除哪几个名称的方法
     */
    String[] excludeMethods() default {};

    InheritFlag[] flags() default {};
}
