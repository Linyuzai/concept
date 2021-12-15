package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class LoadSourceHandler implements DownloadHandler, DownloadContextInitializer {

    private SourceLoader sourceLoader;

    private LoadExceptionHandler loadExceptionHandler;

    @Override
    public void handle(DownloadContext context, DownloadHandlerChain chain) throws IOException {
        Source source = context.get(Source.class);
        Collection<LoadSourceException> loadSourceExceptions = newLoadSourceExceptionContainer();
        context.set(LoadSourceException.class, loadSourceExceptions);
        sourceLoader.load(source, context);
        if (!loadSourceExceptions.isEmpty()) {
            loadExceptionHandler.onLoaded(loadSourceExceptions);
        }
        chain.next(context);
    }

    public Collection<LoadSourceException> newLoadSourceExceptionContainer() {
        return Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public int getOrder() {
        return ORDER_LOAD_SOURCE;
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(LoadExceptionHandler.class, loadExceptionHandler);
    }
}
