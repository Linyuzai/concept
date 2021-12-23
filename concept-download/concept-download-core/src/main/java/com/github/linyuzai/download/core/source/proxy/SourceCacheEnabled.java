package com.github.linyuzai.download.core.source.proxy;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceProxy
public @interface SourceCacheEnabled {

}
