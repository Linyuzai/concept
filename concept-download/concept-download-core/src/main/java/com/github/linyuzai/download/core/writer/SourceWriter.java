package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface SourceWriter extends OrderProvider {

    int ORDER_ORIGINAL_SOURCE = 0;
    int ORDER_COMPRESSED_SOURCE = 100;

    boolean support(Source source, DownloadContext context);

    void write(InputStream is, OutputStream os, Range range, Charset charset) throws IOException;
}
