package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface SourceCompressorAdapter {

    SourceCompressor getCompressor(String format, DownloadContext context);
}
