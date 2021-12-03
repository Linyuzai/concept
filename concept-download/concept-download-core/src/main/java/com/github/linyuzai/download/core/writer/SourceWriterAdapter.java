package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;

import java.nio.charset.Charset;

public interface SourceWriterAdapter {

    SourceWriter getSourceWriter(Source source, Range range, DownloadContext context);
}
