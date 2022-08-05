package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.config.AbstractInstanceConfig;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exception.EventException;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
            EventPublisher publisher = context.get(EventPublisher.class);
            if (publisher == null) {
                //如果没有发布器进行默认发布
                defaultPublish(event, context);
            } else {
                publisher.publish(event, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
        }
    }

    public void defaultPublish(Object event, EventContext context) {
        throw new EventException("EventPublisher is null");
    }

    @Override
    public Subscription subscribe(EventListener listener, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            EventSubscriber subscriber = context.get(EventSubscriber.class);
            if (subscriber == null) {
                //如果没有订阅器进行默认订阅
                return defaultSubscribe(listener, context);
            } else {
                return subscriber.subscribe(listener, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
            return Subscription.EMPTY;
        }
    }

    public Subscription defaultSubscribe(EventListener listener, EventContext context) {
        throw new EventException("EventSubscriber is null");
    }

    @Override
    public int hashCode() {
        return Objects.hash(engine, name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventEndpoint &&
                engine.equals(((EventEndpoint) obj).getEngine()) &&
                name.equals(((EventEndpoint) obj).getName());
    }
}
