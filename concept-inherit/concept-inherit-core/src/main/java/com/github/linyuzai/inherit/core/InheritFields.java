package com.github.linyuzai.inherit.core;

import java.lang.annotation.*;

@Repeatable(InheritFieldsRepeat.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritFields {

    /**
     * 继承属性的源 Class
     */
    Class<?>[] sources();

    /**
     * 是否继承父类属性
     */
    boolean superFields();

    /**
     * 包含哪几个类的属性
     */
    Class<?>[] includes() default {};

    /**
     * 包含哪几个名称的属性
     */
    String[] includeFields() default {};

    /**
     * 排除哪几个类的属性
     */
    Class<?>[] excludes() default {};

    /**
     * 排除哪几个名称的属性
     */
    String[] excludeFields() default {};

    /**
     * 生成器，如生成 Builder 属性的方法，Getter/Setter 方法
     */
    Class<? extends InheritGenerator>[] generators() default {};
}
