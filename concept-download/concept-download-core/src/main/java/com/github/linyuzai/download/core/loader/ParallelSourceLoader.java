package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ParallelSourceLoader implements SourceLoader {

    @Getter
    @Setter
    private boolean serialOnSingle = true;

    @Override
    public void load(Source source, DownloadContext context) {
        Collection<Source> sources = source.flatten();
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
            if (parallelSources.size() == 1 && serialOnSingle) {
                for (Source parallelSource : parallelSources) {
                    parallelSource.load(context);
                }
            } else {
                parallelLoad(parallelSources, context);
            }
        }
        if (!serialSources.isEmpty()) {
            for (Source serialSource : serialSources) {
                serialSource.load(context);
            }
        }
    }

    public abstract void parallelLoad(Collection<Source> sources, DownloadContext context);
}
