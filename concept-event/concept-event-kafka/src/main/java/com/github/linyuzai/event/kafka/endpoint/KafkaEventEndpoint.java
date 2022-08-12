package com.github.linyuzai.event.kafka.endpoint;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.AbstractEventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.kafka.exception.KafkaEventException;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import com.github.linyuzai.event.kafka.publisher.DefaultKafkaEventPublisher;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.transaction.KafkaTransactionManager;

/**
 * 基于 Kafka 的事件端点
 */
@Getter
@Setter
public class KafkaEventEndpoint extends AbstractEventEndpoint {

    /**
     * Kafka 扩展配置
     */
    private KafkaEventProperties.ExtendedKafkaProperties properties;

    private ProducerFactory<Object, Object> producerFactory;

    private ProducerListener<Object, Object> producerListener;

    private KafkaTemplate<Object, Object> template;

    private ConsumerFactory<Object, Object> consumerFactory;

    private KafkaTransactionManager<Object, Object> transactionManager;

    private KafkaListenerContainerFactory<? extends MessageListenerContainer> listenerContainerFactory;

    private KafkaAdmin admin;

    public KafkaEventEndpoint(@NonNull String name, @NonNull EventEngine engine) {
        super(name, engine);
    }

    /**
     * 默认发布
     */
    @Override
    public void defaultPublish(Object event, EventContext context) {
        new DefaultKafkaEventPublisher().publish(event, this, context);
    }

    /**
     * 默认订阅
     */
    @Override
    public Subscription defaultSubscribe(EventListener listener, EventContext context) {
        throw new KafkaEventException("EventSubscriber is null");
    }
}
