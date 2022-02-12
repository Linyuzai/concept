package com.github.linyuzai.download.core.aop.annotation;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;

import java.lang.annotation.*;

/**
 * 和 {@link Download} 组合使用，
 * 可以单独配置 {@link Compression} 的缓存。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CompressCache {

    /**
     * 是否开启压缩文件缓存。
     * 开启后，将会先在本地生成一个压缩文件。
     * 否则，将直接在内存中压缩。
     * 如果文件小可以不开启。
     */
    boolean enabled() default true;

    /**
     * 缓存目录分组。
     * 相当于在配置的缓存目录下新建一个子目录。
     * 方便根据不同业务指定不同的缓存文件夹，
     * 防止文件重名等问题。
     */
    String group() default "";

    /**
     * 缓存的压缩文件名称。
     * 如果不指定，
     * 则使用 {@link CacheNameGenerator#generate(Downloadable, DownloadContext)} 来生成一个名称。
     */
    String name() default "";

    /**
     * 是否删除压缩文件。
     * 如果压缩文件每次的内容都不一样建议删除，
     * 防止因名称相同存在缓存而不会再次压缩。
     */
    boolean delete() default false;
}
