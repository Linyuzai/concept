package com.github.linyuzai.event.kafka.endpoint;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.AbstractEventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
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

import java.lang.reflect.Type;

@Getter
@Setter
public class KafkaEventEndpoint extends AbstractEventEndpoint {

    private KafkaEventProperties.ExtendedKafkaProperties properties;

    private ProducerFactory<Object, Object> producerFactory;

    private ProducerListener<Object, Object> producerListener;

    private KafkaTemplate<Object, Object> template;

    private ConsumerFactory<Object, Object> consumerFactory;

    private KafkaTransactionManager<Object, Object> transactionManager;

    private KafkaListenerContainerFactory<? extends MessageListenerContainer> listenerContainerFactory;

    private KafkaAdmin admin;

    public KafkaEventEndpoint(@NonNull String name) {
        super(name);
    }

    @Override
    public void defaultPublish(Object event, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        new DefaultKafkaEventPublisher(errorHandler).publish(event, this, context);
    }

    @Override
    public void defaultSubscribe(Type type, EventContext context) {
        throw new KafkaEventException("EventSubscriber is null");
    }
}
