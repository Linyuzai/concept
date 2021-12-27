package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

/**
 * 标记一个类作为反射的数据模型 / Mark a class as the data model for reflection
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SourceReflection
public @interface SourceModel {

    /**
     * @return 是否反射父类 / Reflect parent class
     */
    boolean superclass() default true;
}
