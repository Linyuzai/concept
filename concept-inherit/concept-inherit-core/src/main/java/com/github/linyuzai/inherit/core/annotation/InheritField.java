package com.github.linyuzai.inherit.core.annotation;

import com.github.linyuzai.inherit.core.flag.InheritFlag;

import java.lang.annotation.*;

/**
 * 通过该注解来继承指定类的字段
 */
@Repeatable(InheritFields.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritField {

    /**
     * 继承属性的源 Class
     */
    Class<?>[] sources();

    /**
     * 是否继承父类字段
     */
    boolean inheritSuper() default false;

    /**
     * 排除指定名称的字段
     */
    String[] excludeFields() default {};

    /**
     * 继承标识
     */
    InheritFlag[] flags() default {};
}
