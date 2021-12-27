package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.reflect.conversion.StringToCharsetValueConvertor;

import java.lang.annotation.*;
import java.nio.charset.Charset;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(
        methodName = "setCharset",
        methodParameterType = Charset.class,
        fieldName = "charset",
        convertor = StringToCharsetValueConvertor.class)
public @interface SourceCharset {

}
