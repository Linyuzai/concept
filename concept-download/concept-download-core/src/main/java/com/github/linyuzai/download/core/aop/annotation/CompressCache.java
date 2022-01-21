package com.github.linyuzai.download.core.aop.annotation;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 和 {@link Download} 组合使用，
 * 可以单独配置 {@link Compression} 的缓存。
 * <p>
 * Combined with {@link Download},
 * The cache of {@link Compression} can be configured separately.
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
     * <p>
     * Enable compressed file cache.
     * When enabled, a compressed file will be generated locally first.
     * Otherwise, it will be compressed directly in memory.
     * It can not be opened if the file is small.
     */
    boolean enabled() default true;

    /**
     * 缓存目录分组。
     * 相当于在配置的缓存目录下新建一个子目录。
     * 方便根据不同业务指定不同的缓存文件夹，
     * 防止文件重名等问题。
     * <p>
     * Cache directory grouping.
     * This is equivalent to creating a new subdirectory under the configured cache directory.
     * It is convenient to specify different cache folders according to different businesses
     * to prevent file name duplication and other problems.
     */
    String group() default "";

    /**
     * 缓存的压缩文件名称。
     * 如果不指定，首先将使用 {@link Source#getName()}，
     * 如果为 null 则使用 {@link CacheNameGenerator#generate(Downloadable, DownloadContext)} 来生成一个名称。
     * <p>
     * The name of the cached compressed files.
     * If not specified, {@link Source#getName()} will be used first,
     * If null, use {@link CacheNameGenerator#generate(Downloadable, DownloadContext)} to generate a name.
     */
    String name() default "";

    /**
     * 是否删除压缩文件。
     * 如果压缩文件每次的内容都不一样建议删除，
     * 防止因名称相同存在缓存而不会再次压缩。
     * <p>
     * Delete compressed file.
     * If the contents of the compressed file are different every time,
     * it is recommended to delete it to prevent it not compressed
     * because there is a cache with the same name.
     */
    boolean delete() default false;
}
