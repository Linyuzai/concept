package com.github.linyuzai.event.core.config;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Map;

/**
 * 实例配置
 * <p>
 * 事件引擎和事件端点都有很多相同的配置
 * <p>
 * 所以提取出来统一处理
 */
public interface InstanceConfig {

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
