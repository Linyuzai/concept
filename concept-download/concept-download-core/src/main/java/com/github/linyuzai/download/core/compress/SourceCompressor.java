package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;

/**
 * 压缩器 / Compressor to compress source
 */
public interface SourceCompressor extends OrderProvider {

    /**
     * 判断是否支持对应的压缩格式 / Judge whether the corresponding compression format is supported
     *
     * @param format  压缩格式 / Compression format
     * @param context 下载上下文 / Context of download
     * @return 是否支持该压缩格式 / If support this compressed format
     */
    boolean support(String format, DownloadContext context);

    /**
     * 如果支持对应的格式就会调用该方法执行压缩 / This method will be called to perform compression if the corresponding format is supported
     *
     * @param source  {@link Source}
     * @param writer  {@link DownloadWriter}
     * @param context Context of download
     * @return 特定的压缩 / An specific compression
     */
    Compression compress(Source source, DownloadWriter writer, DownloadContext context);
}
