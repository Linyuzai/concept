package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@AllArgsConstructor
public class UncompressedSource extends AbstractCompressedSource {

    private OriginalSource source;

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
    public void write(OutputStream os, Range range, SourceWriter writer) throws IOException {
        source.write(os, range, writer);
    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        source.write(os, range, writer, handler);
    }
}
