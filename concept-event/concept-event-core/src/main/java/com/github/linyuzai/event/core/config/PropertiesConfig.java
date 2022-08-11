package com.github.linyuzai.event.core.config;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * 配置文件配置
 * <p>
 * 事件引擎和事件端点都有很多相同的配置
 * <p>
 * 所以提取出来统一处理
 */
public interface PropertiesConfig {

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

    /**
     * 这里统一为事件引擎或事件端点按照该配置文件对应的属性进行配置
     */
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
