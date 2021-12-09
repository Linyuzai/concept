package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ParallelOriginalSourceLoader implements OriginalSourceLoader {

    @Getter
    @Setter
    private boolean serialOnSingle = true;

    @Override
    public void load(OriginalSource source, DownloadContext context) throws IOException {
        Collection<OriginalSource> sources = source.flatten();
        if (sources.size() <= 1 && serialOnSingle) {
            source.load(context);
            return;
        }
        Collection<OriginalSource> parallelSources = new ArrayList<>();
        Collection<OriginalSource> serialSources = new ArrayList<>();
        for (OriginalSource s : sources) {
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
            for (OriginalSource serialSource : serialSources) {
                serialSource.load(context);
            }
        }
    }

    public abstract void parallelLoad(Collection<OriginalSource> sources, DownloadContext context);
}
