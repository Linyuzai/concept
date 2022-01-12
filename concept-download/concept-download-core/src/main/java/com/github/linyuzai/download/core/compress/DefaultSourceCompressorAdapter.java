package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.order.OrderProvider;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;

/**
 * 默认实现的压缩器适配器 / Adapter of compressor implemented by default
 */
@AllArgsConstructor
public class DefaultSourceCompressorAdapter implements SourceCompressorAdapter {

    private final List<SourceCompressor> compressors;

    public void sort() {
        this.compressors.sort(Comparator.comparingInt(OrderProvider::getOrder));
    }

    /**
     * 根据压缩格式获得对应的压缩器 / Obtain the corresponding compressor according to the compression format
     *
     * @param format  压缩格式 / Format of Compression
     * @param context 下载上下文 / Context of download
     * @return 压缩器 / compressor
     */
    @Override
    public SourceCompressor getCompressor(String format, DownloadContext context) {
        for (SourceCompressor compressor : compressors) {
            if (compressor.support(format, context)) {
                return compressor;
            }
        }
        throw new DownloadException("No SourceCompressor support: " + format);
    }
}
