package com.github.linyuzai.inherit.core.annotation;

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
    boolean inheritSuper() default true;

    /**
     * 排除哪几个名称的方法
     */
    String[] excludeMethods() default {};

    String[] flags() default {};
}
