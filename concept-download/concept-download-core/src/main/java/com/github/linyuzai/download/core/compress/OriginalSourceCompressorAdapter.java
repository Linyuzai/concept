package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface OriginalSourceCompressorAdapter {

    OriginalSourceCompressor getOriginalSourceCompressor(String format, DownloadContext context);
}
