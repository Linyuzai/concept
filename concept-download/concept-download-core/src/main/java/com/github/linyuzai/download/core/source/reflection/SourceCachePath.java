package com.github.linyuzai.download.core.source.reflection;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection
public @interface SourceCachePath {

}
