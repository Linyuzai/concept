package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * {@link SourceCompressor} 适配器实现
 * <p>
 * Adapter implementation of {@link SourceCompressor}
 */
@AllArgsConstructor
public class DefaultSourceCompressorAdapter implements SourceCompressorAdapter {

    private final List<SourceCompressor> compressors;

    /**
     * 根据压缩格式获得对应的 {@link SourceCompressor}。
     * 如果没有可用的 {@link SourceCompressor} 则抛出异常。
     * <p>
     * Obtain the corresponding {@link SourceCompressor} according to the compression format.
     * Throw an exception if no {@link SourceCompressor} is available.
     *
     * @param format  压缩格式
     *                <p>
     *                Compression format
     * @param context {@link DownloadContext}
     * @return 匹配上的 {@link SourceCompressor}
     * <p>
     * Match {@link SourceCompressor}
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
