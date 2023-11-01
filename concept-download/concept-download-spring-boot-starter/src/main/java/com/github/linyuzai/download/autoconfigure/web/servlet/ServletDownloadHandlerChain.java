package com.github.linyuzai.download.autoconfigure.web.servlet;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * {@link DownloadHandlerChain} 的默认实现。
 */
@Getter
@RequiredArgsConstructor
public class ServletDownloadHandlerChain implements DownloadHandlerChain {

    /**
     * 下标，指定处理器位置
     */
    private int index = 0;

    /**
     * 处理器列表
     */
    private final List<DownloadHandler> handlers;

    /**
     * 如果可以获得下一个处理器则调用。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public Object next(DownloadContext context) {
        if (index < handlers.size()) {
            DownloadHandler handler = handlers.get(index++);
            return handler.handle(context, this);
        } else {
            return null;
        }
    }
}
