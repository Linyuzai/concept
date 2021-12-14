package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;

import java.io.IOException;

public interface SourceCompressor extends OrderProvider {

    boolean support(String format, DownloadContext context);

    Compression compress(Source source, DownloadWriter writer, String cachePath, DownloadContext context) throws IOException;
}
