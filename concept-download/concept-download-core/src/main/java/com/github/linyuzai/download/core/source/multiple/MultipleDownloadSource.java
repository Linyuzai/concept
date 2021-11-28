package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.range.RangeReadException;
import com.github.linyuzai.download.core.source.DownloadSource;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
public class MultipleDownloadSource implements DownloadSource {

    private Collection<DownloadSource> sources;

    @Override
    public String getName() {
        if (sources.isEmpty()) {
            return null;
        } else {
            return sources.iterator().next().getName();
        }
    }

    @Override
    public String getCharset() {
        if (sources.size() == 1) {
            return sources.iterator().next().getCharset();
        } else {
            return null;
        }
    }

    @Override
    public long getLength() {
        long length = 0;
        for (DownloadSource source : sources) {
            length += source.getLength();
        }
        return length;
    }

    @Override
    public void load() throws IOException {
        for (DownloadSource source : sources) {
            source.load();
        }
    }

    @Override
    public boolean isAsyncLoad() {
        for (DownloadSource source : sources) {
            if (source.isAsyncLoad()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void read(Reader reader) throws IOException {
        for (DownloadSource source : sources) {
            source.read(reader);
        }
    }

    @Override
    public Collection<DownloadSource> flatten(Predicate<DownloadSource> predicate) {
        List<DownloadSource> all = new ArrayList<>();
        for (DownloadSource source : sources) {
            all.addAll(source.flatten(predicate));
        }
        return all;
    }
}
