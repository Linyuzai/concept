package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;

public interface SourceCompressor {

    boolean support(String format, DownloadContext context);

    CompressedSource compress(OriginalSource source, DownloadContext context);
}
