package com.github.linyuzai.download.aop.annotation;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.Compressible;
import com.github.linyuzai.download.core.contenttype.ContentType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Download {

    String[] source() default {};

    /**
     * 下载是数据对象缓存
     */
    boolean sourceCacheEnabled() default true;

    String sourceCacheGroup() default "";

    boolean deleteSourceCache() default false;

    /**
     * 下载显示的文件名称
     */
    String filename() default "";

    /**
     * Content-Type Header
     */
    String contentType() default ContentType.OCTET_STREAM;

    /**
     * 压缩格式
     */
    String compressFormat() default CompressFormat.ZIP;

    /**
     *
     */
    boolean compressOnSingle() default false;

    /**
     * 压缩目录时是否保持之前的结构
     */
    //boolean compressKeepStruct() default true;

    /**
     * 压缩文件缓存
     */
    boolean compressCacheEnabled() default true;

    String compressCacheGroup() default "";

    String compressCacheName() default Compressible.NAME;

    boolean deleteCompressCache() default false;

    String charset() default "";

    String[] headers() default {};

    /**
     * 额外数据
     */
    String extra() default "";
}
