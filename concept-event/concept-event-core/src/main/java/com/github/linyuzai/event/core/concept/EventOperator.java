package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Map;

public interface EventOperator {

    EventOperator exchange(EventExchange exchange);

    EventOperator encoder(EventEncoder encoder);

    EventOperator decoder(EventDecoder decoder);

    EventOperator error(EventErrorHandler errorHandler);

    EventOperator context(Object key, Object value);

    <K, V> EventOperator context(Map<K, V> context);

    void publish();

    void publish(EventPublisher publisher);

    void subscribe();

    void subscribe(EventSubscriber subscriber);

    interface PropertyConfig {

        Map<Object, Object> getMetadata();

        void setMetadata(Map<Object, Object> metadata);

        Class<? extends EventEncoder> getEncoder();

        void setEncoder(Class<? extends EventEncoder> encoder);

        Class<? extends EventDecoder> getDecoder();

        void setDecoder(Class<? extends EventDecoder> decoder);

        Class<? extends EventErrorHandler> getErrorHandler();

        void setErrorHandler(Class<? extends EventErrorHandler> errorHandler);

        Class<? extends EventPublisher> getPublisher();

        void setPublisher(Class<? extends EventPublisher> publisher);

        Class<? extends EventSubscriber> getSubscriber();

        void setSubscriber(Class<? extends EventSubscriber> subscriber);

        default void apply(ObjectConfig config) {
            config.setMetadata(getMetadata());
            config.setEncoder(newInstance(getEncoder()));
            config.setDecoder(newInstance(getDecoder()));
            config.setErrorHandler(newInstance(getErrorHandler()));
            config.setPublisher(newInstance(getPublisher()));
            config.setSubscriber(newInstance(getSubscriber()));
        }

        @SneakyThrows
        default <T> T newInstance(Class<T> clazz) {
            if (clazz == null) {
                return null;
            }
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
    }

    interface ObjectConfig {
        Map<Object, Object> getMetadata();

        void setMetadata(Map<Object, Object> metadata);

        EventEncoder getEncoder();

        void setEncoder(EventEncoder encoder);

        EventDecoder getDecoder();

        void setDecoder(EventDecoder decoder);

        EventErrorHandler getErrorHandler();

        void setErrorHandler(EventErrorHandler errorHandler);

        EventPublisher getPublisher();

        void setPublisher(EventPublisher publisher);

        EventSubscriber getSubscriber();

        void setSubscriber(EventSubscriber subscriber);
    }
}
