package com.github.linyuzai.event.core.error;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;

/**
 * 基于日志打印的异常处理器
 */
@AllArgsConstructor
public class LoggerEventErrorHandler implements EventErrorHandler {

    private BiConsumer<String, Throwable> errorConsumer;

    @Override
    public void onError(Throwable e, EventEndpoint endpoint, EventContext context) {
        errorConsumer.accept("Event >> error", e);
    }
}
