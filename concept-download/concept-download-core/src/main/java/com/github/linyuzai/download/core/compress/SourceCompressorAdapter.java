package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link SourceCompressor} 适配器。
 * <p>
 * Adapter of {@link SourceCompressor}.
 */
public interface SourceCompressorAdapter {

    /**
     * 根据压缩格式获得对应的压缩器。
     * <p>
     * Obtain the corresponding compressor according to the compression format.
     *
     * @param format  压缩格式
     *                <p>
     *                Compression format
     * @param context {@link DownloadContext}
     * @return 匹配上的 {@link SourceCompressor}
     * <p>
     * Match {@link SourceCompressor}
     */
    SourceCompressor getCompressor(String format, DownloadContext context);
}
