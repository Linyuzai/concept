package com.github.linyuzai.download.core.options;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.DownloadRequest;
import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.Data;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 下载参数。
 *
 * @see Download
 * @see SourceCache
 * @see CompressCache
 */
@Data
public class DefaultDownloadOptions implements ConfigurableDownloadOptions {

    /**
     * 需要下载的原始数据对象
     * 后续会适配解析为 {@link Source}
     */
    Object source;

    /**
     * {@link Source} 是否开启缓存
     * 主要用于需要网络操作，如http等数据的缓存
     */
    boolean sourceCacheEnabled;

    /**
     * {@link Source} 的缓存目录
     */
    String sourceCachePath;

    /**
     * {@link Source} 的缓存在下载结束后是否删除
     */
    boolean sourceCacheDelete;

    /**
     * 下载显示的文件名称，即在浏览器上下载下来显示的名称
     */
    String filename;

    /**
     * 在某些格式下可以直接预览，如图片或视频
     */
    boolean inline;

    /**
     * Content-Type
     */
    String contentType;

    /**
     * 压缩格式
     */
    String compressFormat;

    /**
     * 当只有一个文件时是否强制压缩
     */
    boolean forceCompress;

    /**
     * 是否开启压缩文件缓存
     * 开启后，将会先在本地生成一个压缩文件缓存
     * 如果文件小可以不开启
     */
    boolean compressCacheEnabled;

    /**
     * 压缩文件的缓存目录
     */
    String compressCachePath;

    /**
     * 压缩文件缓存名称
     */
    String compressCacheName;

    /**
     * 是否删除压缩文件
     */
    boolean compressCacheDelete;

    /**
     * 如果指定了编码，会使用字符流的方式读
     */
    Charset charset;

    /**
     * 额外的响应头
     */
    Map<String, String> headers;

    /**
     * Request
     */
    DownloadRequest request;

    /**
     * Response
     */
    DownloadResponse response;

    /**
     * 额外数据
     */
    Object extra;

    /**
     * 下载方法，切面中拦截的方法
     */
    Method method;

    Object returnValue;

    /**
     * 额外的 {@link DownloadEventListener}
     */
    DownloadEventListener eventListener;

    /**
     * {@link DefaultDownloadOptions} 重写器
     */
    public interface Rewriter {

        /**
         * 重写下载参数
         *
         * @param options {@link DefaultDownloadOptions}
         * @return 重写后的 {@link DefaultDownloadOptions}
         */
        DefaultDownloadOptions rewrite(DefaultDownloadOptions options);
    }
}
