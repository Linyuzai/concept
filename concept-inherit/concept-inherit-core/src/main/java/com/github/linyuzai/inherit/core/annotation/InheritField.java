package com.github.linyuzai.inherit.core.annotation;

import java.lang.annotation.*;

@Repeatable(InheritFields.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritField {

    /**
     * 继承属性的源 Class
     */
    Class<?>[] sources();

    /**
     * 是否继承父类属性
     */
    boolean inheritSuper() default true;

    /**
     * 排除哪几个名称的属性
     */
    String[] excludeFields() default {};

    String[] flags() default {};
}
