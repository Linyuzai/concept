package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

/**
 * 标记一个对象为下载源 / Mark an object as the source
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection
public @interface SourceObject {

}
