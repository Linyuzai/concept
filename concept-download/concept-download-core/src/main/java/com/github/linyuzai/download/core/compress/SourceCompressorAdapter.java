package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * 压缩器适配器 / Adapter of compressor
 */
public interface SourceCompressorAdapter {

    /**
     * 根据压缩格式获得对应的压缩器 / Obtain the corresponding compressor according to the compression format
     *
     * @param format  压缩格式 / Format of Compression
     * @param context 下载上下文 / Context of download
     * @return 压缩器 / compressor
     */
    SourceCompressor getCompressor(String format, DownloadContext context);
}
