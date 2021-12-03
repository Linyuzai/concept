package com.github.linyuzai.download.core.interceptor;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;

import java.io.IOException;

/**
 * 下载拦截器
 */
public interface DownloadInterceptor extends OrderProvider {

    int ORDER_INITIALIZE_CONTEXT = Integer.MIN_VALUE + 1;
    int ORDER_CREATE_SOURCE = 0;
    int ORDER_LOAD_SOURCE = 100;
    int ORDER_COMPRESS_SOURCE = 200;
    int ORDER_WRITE_RESPONSE = 300;
    int ORDER_DESTROY_CONTEXT = Integer.MAX_VALUE - 1;

    /**
     * 拦截回调
     *
     * @param context 下载上下文
     * @param chain   下载链
     */
    void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException;
}
