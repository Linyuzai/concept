package com.github.linyuzai.download.core.request;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class ProvideRequestInterceptor implements DownloadInterceptor {

    private DownloadRequestProvider provider;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        DownloadRequest request = provider.getRequest(context);
        context.set(DownloadRequest.class, request);
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_PROVIDE_REQUEST;
    }
}
