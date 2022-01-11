package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;

import java.io.IOException;

/**
 * 下载处理器 / Handler to download
 */
public interface DownloadHandler extends OrderProvider {

    int ORDER_INITIALIZE_CONTEXT = Integer.MIN_VALUE + 100;
    int ORDER_CREATE_SOURCE = 0;
    int ORDER_LOAD_SOURCE = 100;
    int ORDER_COMPRESS_SOURCE = 200;
    int ORDER_WRITE_RESPONSE = 300;
    int ORDER_DESTROY_CONTEXT = Integer.MAX_VALUE - 100;

    /**
     * 执行处理 / Do handle
     *
     * @param context 下载上下文 / Context of download
     * @param chain   处理链 / Chain of handler
     */
    void handle(DownloadContext context, DownloadHandlerChain chain);
}
