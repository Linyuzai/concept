package com.github.linyuzai.download.core.interceptor;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * 下载拦截链实现
 */
@AllArgsConstructor
public class DownloadInterceptorChainImpl implements DownloadInterceptorChain {

    private final int index;

    private final List<DownloadInterceptor> interceptors;

    @Override
    public void next(DownloadContext context) throws IOException {
        if (index < interceptors.size()) {
            DownloadInterceptor interceptor = interceptors.get(index);
            DownloadInterceptorChain chain = new DownloadInterceptorChainImpl(index + 1, interceptors);
            interceptor.intercept(context, chain);
        }
    }
}
