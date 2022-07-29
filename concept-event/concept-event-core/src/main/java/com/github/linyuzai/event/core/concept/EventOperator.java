package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 事件操作者
 */
public interface EventOperator {

    /**
     * 指定事件交换机
     */
    EventOperator exchange(EventExchange exchange);

    /**
     * 指定事件编码器
     */
    EventOperator encoder(EventEncoder encoder);

    /**
     * 指定事件解码器
     */
    EventOperator decoder(EventDecoder decoder);

    /**
     * 异常处理
     */
    EventOperator error(Consumer<Throwable> errorHandler);

    /**
     * 异常处理
     */
    EventOperator error(BiConsumer<Throwable, EventEndpoint> errorHandler);

    /**
     * 指定异常处理器
     */
    EventOperator error(EventErrorHandler errorHandler);

    /**
     * 添加事件上下文
     */
    EventOperator context(Object key, Object value);

    /**
     * 添加事件上下文
     */
    <K, V> EventOperator context(Map<K, V> context);

    /**
     * 发布事件，使用默认发布器
     */
    void publish();

    /**
     * 发布事件
     *
     * @param publisher 事件发布器
     */
    void publish(EventPublisher publisher);

    /**
     * 订阅事件，使用默认订阅器
     */
    void subscribe();

    /**
     * 订阅事件
     *
     * @param subscriber 事件订阅器
     */
    void subscribe(EventSubscriber subscriber);

    /**
     * 配置文件配置
     */
    interface PropertiesConfig {

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

        default void apply(InstanceConfig config) {
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

    /**
     * 实例配置
     */
    interface InstanceConfig {
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
