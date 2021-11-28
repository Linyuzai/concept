package com.github.linyuzai.download.core.interceptor;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;

/**
 * 下载拦截器
 */
public interface DownloadInterceptor {

    int ORDER_PROVIDE_REQUEST = 0;
    int ORDER_PROVIDE_RESPONSE = 100;
    int ORDER_SOURCE_WRITER = 200;
    int ORDER_SOURCE_PREPARE = 300;
    int ORDER_LOAD_SOURCE = 400;
    int ORDER_COMPRESS_SOURCE = 500;
    int ORDER_WRITE_RESPONSE = 600;

    /**
     * 拦截回调
     *
     * @param context 下载上下文
     * @param chain   下载链
     */
    void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException;

    default int getOrder() {
        return 0;
    }
}
