package com.github.linyuzai.download.core.annotation;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 下载注解。
 * 标注了该注解的方法将被拦截。
 * 因为该注解将替换返回值，
 * 所以请标注在 Controller 的 Mapping 方法上。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Download {

    /**
     * 需要下载的原始数据对象或对应格式，
     * 最终将会被处理成 {@link Source}。
     */
    String[] source() default {};

    /**
     * 在某些格式下可以直接预览，如图片或视频。
     */
    boolean inline() default false;

    /**
     * 下载的文件名称，如在浏览器上下载显示的名称。
     */
    String filename() default "";

    /**
     * 'Content-Type' 响应头。
     */
    String contentType() default "";

    /**
     * 压缩格式。
     */
    String compressFormat() default "";

    /**
     * 如果下载的是单个文件默认不会压缩，
     * 该属性可以强制让单个文件的下载也进行压缩。
     */
    boolean forceCompress() default false;

    /**
     * 如果指定了编码，会使用字符流的方式读。
     */
    String charset() default "";

    /**
     * 额外的响应头。
     */
    String[] headers() default {};

    /**
     * 额外数据。
     */
    String extra() default "";
}
