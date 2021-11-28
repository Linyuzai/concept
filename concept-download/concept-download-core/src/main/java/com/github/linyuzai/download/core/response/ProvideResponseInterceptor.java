package com.github.linyuzai.download.core.response;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class ProvideResponseInterceptor implements DownloadInterceptor {

    private DownloadResponseProvider provider;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        DownloadResponse response = provider.getResponse(context);
        context.set(DownloadResponse.class, response);
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_PROVIDE_RESPONSE;
    }
}
