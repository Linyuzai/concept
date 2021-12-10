package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ParallelSourceLoader implements SourceLoader {

    @Getter
    @Setter
    private boolean serialOnSingle = true;

    @Override
    public void load(Source source, DownloadContext context) throws IOException {
        Collection<Source> sources = source.flatten();
        if (sources.size() <= 1 && serialOnSingle) {
            source.load(context);
            return;
        }
        Collection<Source> parallelSources = new ArrayList<>();
        Collection<Source> serialSources = new ArrayList<>();
        for (Source s : sources) {
            if (s.isAsyncLoad()) {
                parallelSources.add(s);
            } else {
                serialSources.add(s);
            }
        }
        if (!parallelSources.isEmpty()) {
            parallelLoad(parallelSources, context);
        }
        if (!serialSources.isEmpty()) {
            for (Source serialSource : serialSources) {
                serialSource.load(context);
            }
        }
    }

    public abstract void parallelLoad(Collection<Source> sources, DownloadContext context);
}
