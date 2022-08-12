package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.config.AbstractInstanceConfig;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exception.EventException;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * 事件端点的抽象类
 */
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventEndpoint extends AbstractInstanceConfig implements EventEndpoint {

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

    @Override
    public void publish(Object event, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            EventEncoder encoder = context.get(EventEncoder.class);
            //编码事件
            //Object encoded = encoder == null ? event : encoder.encode(event, this, context);
            Object encoded = Optional.ofNullable(encoder)
                    .map(it -> it.encode(event, this, context))
                    .orElse(event);
            EventPublisher publisher = context.get(EventPublisher.class);
            if (publisher == null) {
                //如果没有发布器进行默认发布
                defaultPublish(encoded, context);
            } else {
                publisher.publish(encoded, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
        }
    }

    /**
     * 默认发布
     */
    public void defaultPublish(Object event, EventContext context) {
        throw new EventException("EventPublisher is null");
    }

    @Override
    public Subscription subscribe(EventListener listener, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            context.put(Type.class, listener.getType());
            EventListener decodeListener = new EventListener() {

                @Override
                public void onEvent(Object event, EventEndpoint endpoint, EventContext context) {
                    EventDecoder decoder = context.get(EventDecoder.class);
                    //解码事件
                    //Object decoded = decoder == null ? event : decoder.decode(event, endpoint, context);
                    Object decoded = Optional.ofNullable(decoder)
                            .map(it -> it.decode(event, endpoint, context))
                            .orElse(event);
                    listener.onEvent(decoded, endpoint, context);
                }

                @Override
                public Type getType() {
                    return listener.getType();
                }
            };
            EventSubscriber subscriber = context.get(EventSubscriber.class);
            if (subscriber == null) {
                //如果没有订阅器进行默认订阅
                return defaultSubscribe(decodeListener, context);
            } else {
                return subscriber.subscribe(decodeListener, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
            return Subscription.EMPTY;
        }
    }

    /**
     * 默认订阅
     */
    public Subscription defaultSubscribe(EventListener listener, EventContext context) {
        throw new EventException("EventSubscriber is null");
    }

    /**
     * 引擎（名称）和名称组合计算
     */
    @Override
    public int hashCode() {
        return Objects.hash(engine, name);
    }

    /**
     * 引擎（名称）相同并且名称相同
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventEndpoint &&
                engine.equals(((EventEndpoint) obj).getEngine()) &&
                name.equals(((EventEndpoint) obj).getName());
    }
}
