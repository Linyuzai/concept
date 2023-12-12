package com.github.linyuzai.download.core.options;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.DownloadRequest;
import com.github.linyuzai.download.core.web.DownloadResponse;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 下载参数。
 *
 * @see Download
 * @see SourceCache
 * @see CompressCache
 */
public interface DownloadOptions {

    /**
     * 需要下载的原始数据对象
     * 后续会适配解析为 {@link Source}
     */
    Object getSource();

    /**
     * {@link Source} 是否开启缓存
     * 主要用于需要网络操作，如http等数据的缓存
     */
    boolean isSourceCacheEnabled();

    /**
     * {@link Source} 的缓存目录
     */
    String getSourceCachePath();

    /**
     * {@link Source} 的缓存在下载结束后是否删除
     */
    boolean isSourceCacheDelete();

    /**
     * 下载显示的文件名称，即在浏览器上下载下来显示的名称
     */
    String getFilename();

    /**
     * 在某些格式下可以直接预览，如图片或视频
     */
    boolean isInline();

    /**
     * Content-Type
     */
    String getContentType();

    /**
     * 压缩格式
     */
    String getCompressFormat();

    /**
     * 密码
     */
    String getCompressPassword();

    /**
     * 当只有一个文件时是否强制压缩
     */
    boolean isForceCompress();

    /**
     * 是否开启压缩文件缓存
     * 开启后，将会先在本地生成一个压缩文件缓存
     * 如果文件小可以不开启
     */
    boolean isCompressCacheEnabled();

    /**
     * 压缩文件的缓存目录
     */
    String getCompressCachePath();

    /**
     * 压缩文件缓存名称
     */
    String getCompressCacheName();

    /**
     * 是否删除压缩文件
     */
    boolean isCompressCacheDelete();

    /**
     * 如果指定了编码，会使用字符流的方式读
     */
    Charset getCharset();

    /**
     * 额外的响应头
     */
    Map<String, String> getHeaders();

    /**
     * Request
     */
    DownloadRequest getRequest();

    /**
     * Response
     */
    DownloadResponse getResponse();

    /**
     * 额外数据
     */
    Object getExtra();

    /**
     * 下载方法，切面中拦截的方法
     */
    Method getMethod();

    Object getReturnValue();

    /**
     * 额外的 {@link DownloadEventListener}
     */
    DownloadEventListener getEventListener();

    Consumer<DownloadContext> getAsyncConsumer();

    /**
     * {@link DownloadOptions} 重写器
     */
    interface Configurer {

        /**
         * 重写下载参数
         *
         * @param options {@link DownloadOptions}
         */
        void configure(ConfigurableDownloadOptions options);
    }

    static DownloadOptions get(DownloadContext context) {
        return context.get(DownloadOptions.class);
    }
}
