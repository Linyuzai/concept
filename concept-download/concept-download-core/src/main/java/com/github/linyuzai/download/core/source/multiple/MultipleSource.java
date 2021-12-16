package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
public class MultipleSource implements Source {

    private Collection<Source> sources;

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
        for (Source source : sources) {
            length += source.getLength();
        }
        return length;
    }

    @Override
    public boolean isCacheEnabled() {
        for (Source source : sources) {
            if (source.isCacheEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getCachePath() {
        return null;
    }

    @Override
    public boolean isSingle() {
        Boolean single = null;
        for (Source source : sources) {
            if (single == null) {
                single = source.isSingle();
                if (!single) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void load(DownloadContext context) throws IOException {
        for (Source source : sources) {
            source.load(context);
        }
    }

    @Override
    public boolean isAsyncLoad() {
        for (Source source : sources) {
            if (source.isAsyncLoad()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        for (Source source : sources) {
            source.write(os, range, writer, handler);
        }
    }

    @Override
    public Collection<Source> flatten(Predicate<Source> predicate) {
        List<Source> all = new ArrayList<>();
        for (Source source : sources) {
            all.addAll(source.flatten(predicate));
        }
        return all;
    }

    @Override
    public void deleteCache() {
        sources.forEach(Cacheable::deleteCache);
    }
}
