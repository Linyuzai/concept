package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class LoadSourceInterceptor implements DownloadInterceptor, DownloadContextInitializer {

    private SourceLoader sourceLoader;

    private LoadExceptionHandler loadExceptionHandler;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
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
