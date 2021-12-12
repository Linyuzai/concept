package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@AllArgsConstructor
public class InMemoryCompressed extends Compressed {

    private final Source source;

    private final DownloadWriter writer;

    private final AbstractSourceCompressor compressor;

    @Override
    public Charset getCharset() {
        return null;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public boolean isCacheEnabled() {
        return false;
    }

    @Override
    public String getCachePath() {
        return null;
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        compressor.doCompress(source, os, this.writer);
    }
}
