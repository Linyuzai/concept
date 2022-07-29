package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exception.EventException;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 事件端点的抽象类
 */
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventEndpoint implements EventEndpoint {

    /**
     * 端点名称
     */
    @NonNull
    private final String name;

    /**
     * 所属的事件引擎
     */
    @NonNull
    private final EventEngine engine;

    /**
     * 元数据
     */
    private Map<Object, Object> metadata;

    /**
     * 事件编码器
     */
    private EventEncoder encoder;

    /**
     * 事件解码器
     */
    private EventDecoder decoder;

    /**
     * 异常处理器
     */
    private EventErrorHandler errorHandler;

    /**
     * 事件发布器
     */
    private EventPublisher publisher;

    /**
     * 事件订阅器
     */
    private EventSubscriber subscriber;

    @Override
    public void publish(Object event, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            EventEncoder encoder = context.get(EventEncoder.class);
            //编码事件
            Object encodedEvent = encoder == null ? event : encoder.encode(event);
            EventPublisher publisher = context.get(EventPublisher.class);
            if (publisher == null) {
                //如果没有发布器进行默认发布
                defaultPublish(encodedEvent, context);
            } else {
                publisher.publish(encodedEvent, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
        }
    }

    public void defaultPublish(Object event, EventContext context) {
        throw new EventException("EventPublisher is null");
    }

    @Override
    public void subscribe(Type type, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            EventSubscriber subscriber = context.get(EventSubscriber.class);
            if (subscriber == null) {
                //如果没有订阅器进行默认订阅
                defaultSubscribe(type, context);
            } else {
                subscriber.subscribe(type, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
        }
    }

    public void defaultSubscribe(Type type, EventContext context) {
        throw new EventException("EventSubscriber is null");
    }
}
