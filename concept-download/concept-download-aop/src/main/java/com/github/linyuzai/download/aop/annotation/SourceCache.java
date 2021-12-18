package com.github.linyuzai.download.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceCache {

    /**
     * 原始数据对象是否开启缓存 / Whether the cache is enabled for the original source
     * 主要用于一些需要网络IO操作，如http等数据源的缓存 / It is mainly used for caching sources that require network IO operations, such as HTTP
     * 本地文件没有缓存的概念，或者说文件本身就是缓存 / The local file does not have the concept of cache, or the file itself is a cache
     */
    boolean enabled() default true;

    /**
     * 原始数据对象的缓存目录分组 / Cache directory grouping of original source
     * 如用户指定缓存目录为/home/download/cache，该属性配置images / If the user specifies the cache directory as /home/download/cache, this attribute configures images
     * 则最终缓存文件会存储在/home/download/cache/images目录下 / The final cache file will be stored in the /home/download/cache/images
     * 方便根据不同业务指定不同的缓存文件夹，防止文件重名等问题 / It is convenient to specify different cache folders according to different businesses to prevent file name duplication and other problems
     */
    String group() default "";

    /**
     * 原始数据对象的缓存在下载结束后是否删除 / Delete the cache of the original source after downloading
     */
    boolean delete() default false;
}
