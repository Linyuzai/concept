package com.github.linyuzai.download.aop.annotation;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.contenttype.ContentType;

import java.lang.annotation.*;
import java.nio.charset.Charset;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Download {

    String[] sources() default {};

    /**
     * 下载是数据对象缓存
     */
    boolean sourceCacheEnabled() default true;

    /**
     * 下载显示的文件名称
     */
    String filename() default "";

    /**
     * Content-Type Header
     */
    String contentType() default ContentType.OCTET_STREAM;

    boolean compressEnabled() default true;

    /**
     * 压缩格式
     */
    String compressFormat() default CompressFormat.ZIP;

    /**
     * 压缩目录时是否保持之前的结构
     */
    //boolean compressKeepStruct() default true;

    /**
     * 压缩文件缓存
     */
    boolean compressCacheEnabled() default true;

    boolean skipCompressOnSingleSource() default true;

    String charset() default "";

    /**
     * 额外数据
     */
    String extra() default "";
}
