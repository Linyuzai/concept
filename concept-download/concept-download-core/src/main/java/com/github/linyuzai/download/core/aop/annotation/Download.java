package com.github.linyuzai.download.core.aop.annotation;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 下载注解。
 * 因为该注解将替换返回值，
 * 所以请标注在 Controller 的 Mapping 方法上。
 * <p>
 * Annotation for downloading.
 * Because this annotation will replace the return value,
 * please mark it on the mapping method of the controller.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Download {

    /**
     * 需要下载的原始数据对象或对应格式。
     * 最终将会被处理成 {@link Source}。
     * <p>
     * Original data object or corresponding format to be downloaded.
     * It will eventually be processed as {@link Source}.
     */
    String[] source() default {};

    /**
     * 在某些格式下可以直接预览。
     * 如图片或视频。
     * <p>
     * In some formats, you can preview directly.
     * Such as pictures or videos.
     */
    boolean inline() default false;

    /**
     * 下载的文件名称，如在浏览器上下载显示的名称。
     * <p>
     * The name of the downloaded file,
     * such as the name displayed when downloading on the browser.
     */
    String filename() default "";

    /**
     * 'Content-Type' 响应头。
     * <p>
     * The 'Content-Type' header.
     */
    String contentType() default "";

    /**
     * 压缩格式。
     * <p>
     * Compression format.
     */
    String compressFormat() default "";

    /**
     * 如果下载的是单个文件默认不会压缩。
     * 该属性可以强制对单个文件的下载也进行压缩。
     * <p>
     * If you download a single file, it will not be compressed by default.
     * This attribute can force the download of a single file to be compressed.
     */
    boolean forceCompress() default false;

    /**
     * 如果指定了编码，会使用字符流的方式读。
     * <p>
     * If an encoding is specified, it is read using a character stream.
     */
    String charset() default "";

    /**
     * 额外的响应头。
     * <p>
     * Additional response headers.
     */
    String[] headers() default {};

    /**
     * 额外数据。
     * <p>
     * Extra data.
     */
    String extra() default "";
}
