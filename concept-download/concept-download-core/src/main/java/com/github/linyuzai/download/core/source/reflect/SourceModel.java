package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

/**
 * 标记一个类作为反射的数据模型。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SourceReflection
public @interface SourceModel {

    /**
     * 是否反射父类。
     *
     * @return 反射父类则返回 true
     */
    boolean superclass() default true;
}
