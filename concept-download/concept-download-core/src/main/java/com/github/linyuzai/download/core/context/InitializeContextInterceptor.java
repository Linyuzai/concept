package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class InitializeContextInterceptor implements DownloadInterceptor {

    @NonNull
    private List<DownloadContextInitializer> initializers;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        for (DownloadContextInitializer initializer : initializers) {
            initializer.initialize(context);
        }
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_INITIALIZE_CONTEXT;
    }
}
