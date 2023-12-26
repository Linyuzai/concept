package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;

import java.io.IOException;

/**
 * {@link Source} 压缩器。
 *
 * @see ZipSourceCompressor
 */
public interface SourceCompressor {

    /**
     * 获得压缩格式。
     *
     * @return 压缩格式
     */
    String[] getFormats();

    /**
     * 判断是否支持对应的压缩格式。
     *
     * @param format  压缩格式
     * @param context {@link DownloadContext}
     * @return 如果支持则返回 true
     */
    default boolean support(String format, DownloadContext context) {
        for (String supported : getFormats()) {
            if (format.equalsIgnoreCase(supported)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果支持对应的格式就会调用该方法执行压缩。
     *
     * @param source  {@link Source}
     * @param format  压缩格式
     * @param writer  {@link DownloadWriter}
     * @param context {@link DownloadContext}
     * @return {@link Compression}
     */
    Compression compress(Source source, String format, DownloadWriter writer, DownloadContext context) throws IOException;
}
