package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.function.Predicate;

@AllArgsConstructor
public class UncompressedSource implements CompressedSource, OriginalSource {

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
    public void load() throws IOException {
        source.load();
    }

    @Override
    public boolean isAsyncLoad() {
        return source.isAsyncLoad();
    }

    @Override
    public Collection<OriginalSource> flatten() {
        return source.flatten();
    }

    @Override
    public Collection<OriginalSource> flatten(Predicate<OriginalSource> predicate) {
        return source.flatten(predicate);
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
