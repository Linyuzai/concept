package com.github.linyuzai.download.core.options;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import lombok.Builder;
import lombok.Value;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 下载操作参数 / Download options
 */
@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class DownloadOptions {

    /**
     * 需要下载是原始数据对象 / Original source to download
     */
    Object source;

    /**
     * 原始数据对象是否开启缓存 / Whether the cache is enabled for the original source
     * 主要用于一些需要网络IO操作，如http等数据源的缓存 / It is mainly used for caching sources that require network IO operations, such as HTTP
     * 本地文件没有缓存的概念，或者说文件本身就是缓存 / The local file does not have the concept of cache, or the file itself is a cache
     */
    boolean sourceCacheEnabled;

    /**
     * 原始数据对象的缓存目录 / Cache directory of original source
     */
    String sourceCachePath;

    /**
     * 原始数据对象的缓存在下载结束后是否删除 / Delete the cache of the original source after downloading
     */
    boolean sourceCacheDelete;

    /**
     * 下载显示的文件名称，在浏览器上下载下来显示的名称 / Download the displayed file name, and download the displayed name on the browser
     */
    String filename;

    /**
     * 在某些格式下可以直接预览 / In some formats, you can preview directly
     * 如图片或视频 / Such as pictures or videos
     */
    boolean inline;

    /**
     * Content-Type Header
     */
    String contentType;

    /**
     * 压缩格式 / Compression format
     */
    String compressFormat;

    /**
     * 当只有一个数据源是否强制压缩 / Whether to force compression when there is only one source
     * 单个文件目录不适用 / Single directory not applicable
     */
    boolean forceCompress;

    /**
     * 是否开启压缩文件缓存 / Enable compressed file cache
     * 开启后，将会先在本地生成一个压缩文件 / When enabled, a compressed file will be generated locally first
     * 否则，讲直接写入输出流 / Otherwise, write the output stream directly
     * 如果文件小可以不开启 / It can not be opened if the file is small
     */
    boolean compressCacheEnabled;

    /**
     * 压缩文件的缓存目录 / Cache directory of compressed files
     * 同 {@link DownloadOptions#sourceCachePath} / Same as {@link DownloadOptions#sourceCachePath}
     */
    String compressCachePath;

    /**
     * 压缩文件名称 / Compressed file name
     * 单下载源会使用该下载源的名名称 / A single source will use the name of the source
     * 多下载源会使用第一个有名称的下载源的名称 / Multiple sources will use the name of the first source with a name
     * 否则使用缓存名称生成器生成，默认使用时间戳 / Otherwise, it is generated using the cache name generator, and the timestamp is used by default
     *
     * @see CacheNameGenerator
     */
    String compressCacheName;

    /**
     * 是否删除压缩文件 / If delete compressed file
     * 如果压缩文件每次的内容都不一样建议删除 / If the contents of the compressed file are different every time, it is recommended to delete it
     * 防止因名称相同存在缓存而不会再次压缩 / Prevents the cache from being compressed again because it has the same name
     */
    boolean compressCacheDelete;

    /**
     * 如果指定了编码，会使用字符流的方式读 / If an encoding is specified, it is read using a character stream
     */
    Charset charset;

    /**
     * 额外的响应头 / Additional response headers
     */
    Map<String, String> headers;

    /**
     * 提供支持任意Request的接口 / Provide an interface that supports any request
     */
    Object request;

    /**
     * 提供支持任意Response的接口 / Provide an interface that supports any response
     */
    Object response;

    /**
     * 额外数据 / extra data
     */
    Object extra;

    /**
     * 下载方法，主要用于切面 / Download method, mainly used for aop
     */
    DownloadMethod downloadMethod;

    /**
     * 处理链的拦截器 / Interceptor of handler
     */
    DownloadEventListener eventListener;

    public static DownloadOptions from(DownloadConfiguration configuration) {
        return new DownloadOptions.Builder()
                .headers(configuration.getResponse().getHeaders())
                .sourceCacheEnabled(configuration.getSource().getCache().isEnabled())
                .sourceCachePath(configuration.getSource().getCache().getPath())
                .sourceCacheDelete(configuration.getSource().getCache().isDelete())
                .compressFormat(configuration.getCompress().getFormat())
                .compressCacheEnabled(configuration.getCompress().getCache().isEnabled())
                .compressCachePath(configuration.getCompress().getCache().getPath())
                .compressCacheDelete(configuration.getCompress().getCache().isDelete())
                .build();
    }

    /**
     * 重写器 / Rewriter
     */
    public interface Rewriter {

        /**
         * 重写下载参数 / Rewrite download options
         *
         * @param options 下载的参数 / Download options
         * @return 重写后的参数 / Download options which is rewritten
         */
        DownloadOptions rewrite(DownloadOptions options);
    }
}
