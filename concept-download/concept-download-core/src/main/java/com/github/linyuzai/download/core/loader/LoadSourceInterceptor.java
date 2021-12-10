package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class LoadSourceInterceptor implements DownloadInterceptor {

    private SourceLoader loader;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        Source source = context.get(Source.class);
        loader.load(source, context);
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_LOAD_SOURCE;
    }
}
