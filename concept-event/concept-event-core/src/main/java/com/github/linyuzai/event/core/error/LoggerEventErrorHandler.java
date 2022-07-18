package com.github.linyuzai.event.core.error;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;

@AllArgsConstructor
public class LoggerEventErrorHandler implements EventErrorHandler {

    private BiConsumer<String, Throwable> errorConsumer;

    @Override
    public void onError(Throwable e, EventEndpoint endpoint) {
        errorConsumer.accept("Event >> ", e);
    }
}
