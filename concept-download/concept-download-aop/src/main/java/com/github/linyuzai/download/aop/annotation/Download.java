package com.github.linyuzai.download.aop.annotation;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.contenttype.ContentType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Download {

    String[] original() default {};

    /**
     * 下载是数据对象缓存
     */
    boolean originalCacheEnabled() default true;

    String originalCacheGroup() default "";

    boolean deleteOriginalCache() default false;

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
     *
     */
    boolean skipCompressOnSingle() default true;

    /**
     * 压缩目录时是否保持之前的结构
     */
    //boolean compressKeepStruct() default true;

    /**
     * 压缩文件缓存
     */
    boolean compressCacheEnabled() default true;

    String compressCacheGroup() default "";

    String compressCacheName() default CompressedSource.NAME;

    boolean deleteCompressCache() default false;

    String charset() default "";

    String[] headers() default {};

    /**
     * 额外数据
     */
    String extra() default "";
}
