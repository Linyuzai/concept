package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

/**
 * Kafka 事件订阅器抽象类
 */
public abstract class AbstractKafkaEventSubscriber extends KafkaEventSubscriber {

    @Override
    public Subscription doSubscribe(EventListener listener, KafkaEventEndpoint endpoint, EventContext context) {
        MessageListenerContainer container = createMessageListenerContainer(endpoint, context);
        container.setupMessageListener(createMessageListener(listener, endpoint, context));
        container.start();
        return new KafkaSubscription(container);
    }

    /**
     * 创建消息监听器容器
     */
    public abstract MessageListenerContainer createMessageListenerContainer(KafkaEventEndpoint endpoint, EventContext context);

    /**
     * 创建消息监听器
     */
    public MessageListener<?, ?> createMessageListener(EventListener listener, KafkaEventEndpoint endpoint, EventContext context) {
        ContainerProperties.AckMode mode = endpoint.getProperties().getListener().getAckMode();
        if (mode == ContainerProperties.AckMode.MANUAL || mode == ContainerProperties.AckMode.MANUAL_IMMEDIATE) {
            return (AcknowledgingMessageListener<Object, Object>) (data, acknowledgment) ->
                    handleMessage(data, listener, endpoint, context, acknowledgment::acknowledge);
        } else {
            return (MessageListener<Object, Object>) data ->
                    handleMessage(data, listener, endpoint, context, null);
        }
    }

    /**
     * 处理消息
     */
    public void handleMessage(ConsumerRecord<Object, Object> data,
                              EventListener listener,
                              KafkaEventEndpoint endpoint,
                              EventContext context,
                              Runnable runnable) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            //回调
            listener.onEvent(getPayload(data, endpoint, context), endpoint, context);
            //后置执行，用于手动ack
            if (runnable != null) {
                runnable.run();
            }
        } catch (Throwable e) {
            errorHandler.onError(e, endpoint, context);
        }
    }

    /**
     * 获得数据内容
     */
    public Object getPayload(ConsumerRecord<?, ?> record, KafkaEventEndpoint endpoint, EventContext context) {
        return record.value();
    }
}
