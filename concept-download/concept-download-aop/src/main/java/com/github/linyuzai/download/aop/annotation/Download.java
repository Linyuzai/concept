package com.github.linyuzai.download.aop.annotation;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.contenttype.ContentType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Download {

    String[] source() default {};

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
    boolean forceCompress() default false;

    /**
     * 压缩目录时是否保持之前的结构
     */
    //boolean compressKeepStruct() default true;

    String charset() default "";

    String[] headers() default {};

    /**
     * 额外数据
     */
    String extra() default "";
}
