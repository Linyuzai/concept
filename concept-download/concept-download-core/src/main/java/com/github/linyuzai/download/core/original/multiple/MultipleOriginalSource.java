package com.github.linyuzai.download.core.original.multiple;

import com.github.linyuzai.download.core.cache.AbstractCacheableSource;
import com.github.linyuzai.download.core.cache.CacheableSource;
import com.github.linyuzai.download.core.context.DownloadContext;
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
public class MultipleOriginalSource extends AbstractCacheableSource implements OriginalSource {

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
    public boolean isCacheEnabled() {
        for (OriginalSource source : sources) {
            if (source.isCacheEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setCacheEnabled(boolean cacheEnabled) {

    }

    @Override
    public String getCachePath() {
        return null;
    }

    @Override
    public void setCachePath(String cachePath) {

    }

    @Override
    public boolean isSingle() {
        Boolean single = null;
        for (OriginalSource source : sources) {
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
        for (OriginalSource source : sources) {
            source.load(context);
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

    @Override
    public void deleteCache() {
        sources.forEach(CacheableSource::deleteCache);
    }
}
