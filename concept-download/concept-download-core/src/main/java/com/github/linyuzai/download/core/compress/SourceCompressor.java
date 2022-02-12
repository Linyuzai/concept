package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;

/**
 * 压缩器。
 * <p>
 * Compressor to compress source.
 */
public interface SourceCompressor extends OrderProvider {

    String getFormat();

    /**
     * 判断是否支持对应的压缩格式。
     * <p>
     * Judge whether the corresponding compression format is supported.
     *
     * @param format  压缩格式
     *                <p>
     *                Compression format
     * @param context {@link DownloadContext}
     * @return 如果支持则返回 true
     * <p>
     * Return true if supported
     */
    default boolean support(String format, DownloadContext context) {
        return format.equalsIgnoreCase(getFormat());
    }

    /**
     * 如果支持对应的格式就会调用该方法执行压缩。
     * <p>
     * This method will be called to perform compression
     * if the corresponding format is supported.
     *
     * @param source  {@link Source}
     * @param writer  {@link DownloadWriter}
     * @param context {@link DownloadContext}
     * @return {@link Compression}
     */
    Compression compress(Source source, DownloadWriter writer, DownloadContext context);
}
