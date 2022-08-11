package com.github.linyuzai.event.core.error;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

/**
 * 异常处理器
 * <p>
 * 在事件发布或订阅的过程中所产生的异常将会全部回调到异常处理器
 */
public interface EventErrorHandler {

    /**
     * 异常处理
     *
     * @param e        异常
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    void onError(Throwable e, EventEndpoint endpoint, EventContext context);
}
