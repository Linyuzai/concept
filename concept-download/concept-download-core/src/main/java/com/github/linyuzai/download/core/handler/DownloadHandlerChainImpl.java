package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * 下载处理链默认实现
 */
@AllArgsConstructor
public class DownloadHandlerChainImpl implements DownloadHandlerChain {

    private int index;

    private final List<DownloadHandler> handlers;

    /**
     * 对上一个处理器进行拦截 / Intercept the previous handler
     * 获取下一个处理器 / Get next handler
     * 执行处理器 / Execute handler
     *
     * @param context 下载上下文 / Context of download
     * @throws IOException I/O exception
     */
    @Override
    public void next(DownloadContext context) throws IOException {
        if (index < handlers.size()) {
            DownloadHandler handler = handlers.get(index++);
            //DownloadHandlerChain chain = new DownloadHandlerChainImpl(index + 1, handlers);
            handler.handle(context, this);
        }
    }
}
