package com.github.linyuzai.download.aop.annotation;

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
    String contentType() default "";

    /**
     * 压缩格式
     */
    String compressFormat() default "";

    /**
     *
     */
    boolean forceCompress() default false;

    String charset() default "";

    String[] headers() default {};

    /**
     * 额外数据
     */
    String extra() default "";
}
