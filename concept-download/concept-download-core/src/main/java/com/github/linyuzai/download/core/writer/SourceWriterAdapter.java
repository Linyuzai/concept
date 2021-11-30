package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public interface SourceWriterAdapter {

    SourceWriter getSourceWriter(Source source, DownloadContext context);
}
