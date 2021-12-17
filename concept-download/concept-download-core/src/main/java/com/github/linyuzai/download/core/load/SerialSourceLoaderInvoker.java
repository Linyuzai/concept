package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SerialSourceLoaderInvoker implements SourceLoaderInvoker {

    @Override
    public Collection<SourceLoadResult> invoke(Collection<? extends SourceLoader> loaders, DownloadContext context) throws IOException {
        Collection<SourceLoadResult> results = new ArrayList<>();
        for (SourceLoader loader : loaders) {
            SourceLoadResult result = loader.load(context);
            results.add(result);
        }
        return results;
    }
}
