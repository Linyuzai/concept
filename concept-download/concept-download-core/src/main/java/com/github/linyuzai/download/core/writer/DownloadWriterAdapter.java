package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.concept.Downloadable;

public interface DownloadWriterAdapter {

    DownloadWriter getWriter(Downloadable downloadable, Range range, DownloadContext context);
}
