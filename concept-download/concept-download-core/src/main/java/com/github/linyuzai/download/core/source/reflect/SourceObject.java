package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

/**
 * 标记一个对象为需要下载的原始数据对象。
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection
public @interface SourceObject {

}
