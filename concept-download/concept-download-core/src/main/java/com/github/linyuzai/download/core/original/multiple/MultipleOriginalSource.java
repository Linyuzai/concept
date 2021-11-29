package com.github.linyuzai.download.core.original.multiple;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
public class MultipleOriginalSource implements OriginalSource {

    private Collection<OriginalSource> sources;

    @Override
    public String getName() {
        if (sources.isEmpty()) {
            return null;
        } else {
            return sources.iterator().next().getName();
        }
    }

    @Override
    public Charset getCharset() {
        if (sources.size() == 1) {
            return sources.iterator().next().getCharset();
        } else {
            return null;
        }
    }

    @Override
    public long getLength() {
        long length = 0;
        for (OriginalSource source : sources) {
            length += source.getLength();
        }
        return length;
    }

    @Override
    public void load() throws IOException {
        for (OriginalSource source : sources) {
            source.load();
        }
    }

    @Override
    public boolean isAsyncLoad() {
        for (OriginalSource source : sources) {
            if (source.isAsyncLoad()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        for (OriginalSource source : sources) {
            source.write(os, range, writer, handler);
        }
    }

    @Override
    public Collection<OriginalSource> flatten(Predicate<OriginalSource> predicate) {
        List<OriginalSource> all = new ArrayList<>();
        for (OriginalSource source : sources) {
            all.addAll(source.flatten(predicate));
        }
        return all;
    }
}
