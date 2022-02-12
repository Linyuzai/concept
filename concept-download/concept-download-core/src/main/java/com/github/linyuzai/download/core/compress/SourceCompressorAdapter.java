package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link SourceCompressor} 适配器。
 */
public interface SourceCompressorAdapter {

    /**
     * 根据压缩格式获得对应的压缩器。
     *
     * @param format  压缩格式
     * @param context {@link DownloadContext}
     * @return 匹配上的 {@link SourceCompressor}
     */
    SourceCompressor getCompressor(String format, DownloadContext context);
}
