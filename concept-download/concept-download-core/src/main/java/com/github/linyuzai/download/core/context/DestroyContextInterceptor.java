package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class DestroyContextInterceptor implements DownloadInterceptor {

    @NonNull
    private List<DownloadContextDestroyer> destroyers;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        for (DownloadContextDestroyer destroyer : destroyers) {
            destroyer.destroy(context);
        }
        chain.next(context);
        context.destroy();
    }

    @Override
    public int getOrder() {
        return ORDER_DESTROY_CONTEXT;
    }
}
