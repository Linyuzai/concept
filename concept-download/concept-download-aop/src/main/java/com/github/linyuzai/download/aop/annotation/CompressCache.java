package com.github.linyuzai.download.aop.annotation;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CompressCache {

    /**
     * 是否开启压缩文件缓存 / Enable compressed file cache
     * 开启后，将会先在本地生成一个压缩文件 / When enabled, a compressed file will be generated locally first
     * 否则，讲直接写入输出流 / Otherwise, write the output stream directly
     * 如果文件小可以不开启 / It can not be opened if the file is small
     */
    boolean enabled() default true;

    /**
     * 压缩文件的缓存目录分组 / Cache directory grouping of compressed files
     * 同 {@link SourceCache#group()} / Same as {@link SourceCache#group()}
     */
    String group() default "";

    /**
     * 压缩文件名称 / Compressed file name
     * 单下载源会使用该下载源的名名称 / A single source will use the name of the source
     * 多下载源会使用第一个有名称的下载源的名称 / Multiple sources will use the name of the first source with a name
     * 否则使用缓存名称生成器生成 / Otherwise, it is generated using the cache name generator
     *
     * @see CacheNameGenerator
     */
    String name() default "";

    /**
     * 是否删除压缩文件 / If delete compressed file
     * 如果压缩文件每次的内容都不一样建议删除 / If the contents of the compressed file are different every time, it is recommended to delete it
     * 防止因名称相同存在缓存而不会再次压缩 / Prevents the cache from being compressed again because it has the same name
     */
    boolean delete() default false;
}
