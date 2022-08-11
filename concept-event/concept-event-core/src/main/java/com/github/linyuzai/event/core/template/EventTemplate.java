package com.github.linyuzai.event.core.template;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 事件模版
 * <p>
 * 可对不同的场景配置不同的事件模版进行应用
 */
public interface EventTemplate {

    /**
     * 指定事件交换机
     */
    EventTemplate exchange(EventExchange exchange);

    /**
     * 指定事件编码器
     */
    EventTemplate encoder(EventEncoder encoder);

    /**
     * 指定事件解码器
     */
    EventTemplate decoder(EventDecoder decoder);

    /**
     * 指定事件发布器
     */
    EventTemplate publisher(EventPublisher publisher);

    /**
     * 指定事件订阅者
     */
    EventTemplate subscriber(EventSubscriber subscriber);

    /**
     * 异常处理
     */
    EventTemplate error(Consumer<Throwable> errorHandler);

    /**
     * 异常处理
     */
    EventTemplate error(BiConsumer<Throwable, EventEndpoint> errorHandler);

    /**
     * 指定异常处理器
     */
    EventTemplate error(EventErrorHandler errorHandler);

    /**
     * 添加事件上下文
     */
    EventTemplate context(Object key, Object value);

    /**
     * 发布事件
     */
    void publish(Object event);

    /**
     * 订阅事件
     */
    Subscription subscribe(EventListener listener);
}
