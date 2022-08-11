package com.github.linyuzai.event.core.template;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 基于上下文的事件模版
 * <p>
 * 所有的配置都设置到上下文中
 */
public abstract class ContextEventTemplate implements EventTemplate {

    protected abstract EventContext getContext();

    @Override
    public EventTemplate exchange(EventExchange exchange) {
        return context(EventExchange.class, exchange);
    }

    @Override
    public EventTemplate encoder(EventEncoder encoder) {
        return context(EventEncoder.class, encoder);
    }

    @Override
    public EventTemplate decoder(EventDecoder decoder) {
        return context(EventDecoder.class, decoder);
    }

    @Override
    public EventTemplate publisher(EventPublisher publisher) {
        return context(EventPublisher.class, publisher);
    }

    @Override
    public EventTemplate subscriber(EventSubscriber subscriber) {
        return context(EventSubscriber.class, subscriber);
    }

    @Override
    public EventTemplate error(Consumer<Throwable> errorHandler) {
        return error((e, endpoint, context) -> errorHandler.accept(e));
    }

    @Override
    public EventTemplate error(BiConsumer<Throwable, EventEndpoint> errorHandler) {
        return error((e, endpoint, context) -> errorHandler.accept(e, endpoint));
    }

    @Override
    public EventTemplate error(EventErrorHandler errorHandler) {
        return context(EventErrorHandler.class, errorHandler);
    }

    @Override
    public EventTemplate context(Object key, Object value) {
        getContext().put(key, value);
        return this;
    }
}
