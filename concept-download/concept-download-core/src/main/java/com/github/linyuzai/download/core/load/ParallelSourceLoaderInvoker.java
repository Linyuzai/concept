package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ParallelSourceLoaderInvoker implements SourceLoaderInvoker {

    @Getter
    @Setter
    private boolean serialOnSingle = true;

    @Override
    public Collection<LoadResult> invoke(Collection<? extends SourceLoader> loaders, DownloadContext context) throws IOException {
        Collection<SourceLoader> parallelSourceLoaders = new ArrayList<>();
        Collection<SourceLoader> serialSourceLoaders = new ArrayList<>();
        for (SourceLoader loader : loaders) {
            if (loader.isAsyncLoad()) {
                parallelSourceLoaders.add(loader);
            } else {
                serialSourceLoaders.add(loader);
            }
        }
        Collection<LoadResult> results = new ArrayList<>();
        if (!parallelSourceLoaders.isEmpty()) {
            if (parallelSourceLoaders.size() == 1 && serialOnSingle) {
                for (SourceLoader loader : parallelSourceLoaders) {
                    LoadResult result = loader.load(context);
                    results.add(result);
                }
            } else {
                results.addAll(parallelInvoke(parallelSourceLoaders, context));
            }
        }
        if (!serialSourceLoaders.isEmpty()) {
            for (SourceLoader loader : serialSourceLoaders) {
                LoadResult result = loader.load(context);
                results.add(result);
            }
        }
        return results;
    }

    public abstract Collection<LoadResult> parallelInvoke(Collection<SourceLoader> loaders, DownloadContext context);
}
