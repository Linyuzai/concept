package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LoadSourceHandler implements DownloadHandler {

    private SourceLoaderInvoker sourceLoaderInvoker;

    private SourceLoaderFactory sourceLoaderFactory;

    private SourceLoadExceptionHandler sourceLoadExceptionHandler;

    @Override
    public void handle(DownloadContext context, DownloadHandlerChain chain) throws IOException {
        Source source = context.get(Source.class);
        Collection<SourceLoader> loaders = new ArrayList<>();
        Collection<Source> sources = source.flatten();
        for (Source s : sources) {
            SourceLoader loader = sourceLoaderFactory.create(s, context);
            loaders.add(loader);
        }
        Collection<SourceLoadResult> results = sourceLoaderInvoker.invoke(loaders, context);
        List<SourceLoadException> exceptions = results.stream()
                .filter(SourceLoadResult::hasException)
                .map(SourceLoadResult::getException)
                .collect(Collectors.toList());
        if (!exceptions.isEmpty()) {
            sourceLoadExceptionHandler.onLoaded(exceptions);
        }
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_LOAD_SOURCE;
    }
}
