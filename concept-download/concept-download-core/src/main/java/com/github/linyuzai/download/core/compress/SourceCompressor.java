package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;

/**
 * {@link Source} 压缩器。
 *
 * @see ZipSourceCompressor
 */
public interface SourceCompressor extends OrderProvider {

    /**
     * 获得压缩格式。
     *
     * @return 压缩格式
     */
    String getFormat();

    /**
     * 判断是否支持对应的压缩格式。
     *
     * @param format  压缩格式
     * @param context {@link DownloadContext}
     * @return 如果支持则返回 true
     */
    default boolean support(String format, DownloadContext context) {
        return format.equalsIgnoreCase(getFormat());
    }

    /**
     * 如果支持对应的格式就会调用该方法执行压缩。
     *
     * @param source  {@link Source}
     * @param writer  {@link DownloadWriter}
     * @param context {@link DownloadContext}
     * @return {@link Compression}
     */
    Compression compress(Source source, DownloadWriter writer, DownloadContext context);
}
