package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.original.OriginalSource;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class LoadOriginalSourceInterceptor implements DownloadInterceptor {

    private OriginalSourceLoader loader;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        OriginalSource source = context.get(OriginalSource.class);
        loader.load(source, context);
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_LOAD_SOURCE;
    }
}
