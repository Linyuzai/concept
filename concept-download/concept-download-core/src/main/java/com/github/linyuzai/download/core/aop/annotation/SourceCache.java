package com.github.linyuzai.download.core.aop.annotation;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 和 {@link Download} 组合使用，
 * 可以单独配置 {@link Source} 的缓存。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceCache {

    /**
     * 对于一些需要通过网络读取的下载数据，如http资源。
     * 建议开启缓存，将数据缓存到本地。
     * 默认情况下，将会全部加载到内存中，
     * 并且在下载完成之后释放内存。
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
     * 缓存在下载结束后是否删除。
     */
    boolean delete() default false;
}
