package com.github.linyuzai.download.aop.annotation;

import java.lang.annotation.*;

/**
 * 下载注解 / Annotation for downloading
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Download {

    /**
     * 需要下载的原始数据对象 / Original source to download
     */
    String[] source() default {};

    /**
     * 在某些格式下可以直接预览 / In some formats, you can preview directly
     * 如图片或视频 / Such as pictures or videos
     */
    boolean inline() default false;

    /**
     * 下载显示的文件名称，在浏览器上下载下来显示的名称 / Download the displayed file name, and download the displayed name on the browser
     */
    String filename() default "";

    /**
     * Content-Type Header
     */
    String contentType() default "";

    /**
     * 压缩格式 / Compression format
     */
    String compressFormat() default "";

    /**
     * 当只有一个数据源是否强制压缩 / Whether to force compression when there is only one source
     * 单个文件目录不适用 / Single directory not applicable
     */
    boolean forceCompress() default false;

    /**
     * 如果指定了编码，会使用字符流的方式读 / If an encoding is specified, it is read using a character stream
     */
    String charset() default "";

    /**
     * 额外的响应头 / Additional response headers
     */
    String[] headers() default {};

    /**
     * 额外数据 / extra data
     */
    String extra() default "";
}
