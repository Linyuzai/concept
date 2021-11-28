package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;

public interface SourceCompressor {

    boolean support(String format, DownloadContext context);

    CompressedSource compress(DownloadSource source, DownloadContext context);
}
