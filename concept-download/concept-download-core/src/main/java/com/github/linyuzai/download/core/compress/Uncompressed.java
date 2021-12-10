package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@AllArgsConstructor
public class Uncompressed extends Compressed {

    private Source source;

    @Override
    public String getName() {
        return source.getName();
    }

    @Override
    public Charset getCharset() {
        return source.getCharset();
    }

    @Override
    public long getLength() {
        return source.getLength();
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer) throws IOException {
        source.write(os, range, writer);
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        source.write(os, range, writer, handler);
    }
}
