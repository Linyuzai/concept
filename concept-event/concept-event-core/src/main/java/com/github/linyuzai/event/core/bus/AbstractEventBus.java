package com.github.linyuzai.event.core.bus;

import com.github.linyuzai.event.core.codec.SerializationEventDecoder;
import com.github.linyuzai.event.core.codec.SerializationEventEncoder;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.config.AbstractInstanceConfig;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.core.template.EventTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

/**
 * 事件总线抽象类
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventBus extends AbstractInstanceConfig implements EventBus {

    /**
     * 事件概念
     */
    private final EventConcept concept;

    /**
     * 事件交换机
     */
    private final EventExchange exchange;

    /**
     * 事件模版
     */
    private EventTemplate template;

    /**
     * 订阅句柄
     */
    private Subscription subscription;

    /**
     * 初始化
     * <p>
     * 如已初始化则直接返回
     * <p>
     * 通过基于配置生成一个事件模版
     * <p>
     * 同时执行订阅
     */
    @Override
    public synchronized void initialize() {
        if (subscription != null) {
            return;
        }
        template = concept.template()
                .exchange(exchange)
                //这里默认使用序列化的编码器和解码器
                .encoder(getEncoder() == null ? new SerializationEventEncoder() : getEncoder())
                .decoder(getDecoder() == null ? new SerializationEventDecoder() : getDecoder())
                .publisher(getPublisher())
                .subscriber(getSubscriber())
                .error(getErrorHandler());
        subscription = subscribe();
    }

    /**
     * 销毁
     * <p>
     * 取消订阅
     */
    @Override
    public synchronized void destroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    /**
     * 发布事件
     */
    @Override
    public void publish(Object event) {
        template.publish(event);
        onPublish(event);
    }

    /**
     * 执行订阅
     * <p>
     * 将返回数据回调到 {@link AbstractEventBus#onEvent(Object)}
     */
    public Subscription subscribe() {
        return template.subscribe(new EventListener() {

            @Override
            public void onEvent(Object event, EventEndpoint endpoint, EventContext context) {
                AbstractEventBus.this.onEvent(event);
            }

            @Override
            public Type getType() {
                return null;
            }
        });
    }

    /**
     * 事件发布
     */
    public abstract void onPublish(Object event);

    /**
     * 接收事件
     */
    public abstract void onEvent(Object event);
}
