package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;

@Deprecated
@AllArgsConstructor
@NoArgsConstructor
public class BufferedCompressedSourceWriter implements SourceWriter {

    private int bufferSize = 8 * 1024;

    @Override
    public boolean support(Source source, Range range, DownloadContext context) {
        return source instanceof CompressedSource;
    }

    @Override
    public void write(InputStream is, OutputStream os, Range range, Charset charset) throws IOException {
        int len;
        byte[] bytes = new byte[bufferSize];
        while ((len = is.read(bytes)) > 0) {
            os.write(bytes, 0, len);
        }
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESSED_SOURCE;
    }
}
