package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.endpoint.AbstractEventEndpoint;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.transaction.KafkaTransactionManager;

public class KafkaEventEndpoint extends AbstractEventEndpoint {

    @Getter
    @Setter
    private ProducerFactory<Object, Object> producerFactory;

    @Getter
    @Setter
    private ProducerListener<Object, Object> producerListener;

    @Getter
    @Setter
    private KafkaTemplate<Object, Object> template;

    @Getter
    @Setter
    private ConsumerFactory<Object, Object> consumerFactory;

    @Getter
    @Setter
    private KafkaTransactionManager<Object, Object> transactionManager;

    @Getter
    @Setter
    private KafkaListenerContainerFactory<? extends MessageListenerContainer> listenerContainerFactory;

    @Getter
    @Setter
    private KafkaAdmin admin;

    @Override
    public void publish(Object event, EventPublisher publisher, EventErrorHandler errorHandler) {
        try {
            if (publisher == null) {
                new DefaultKafkaEventPublisher(errorHandler).publish(event, this);
            } else {
                publisher.publish(event, this);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this);
        }
    }

    @Override
    public void subscribe(EventSubscriber subscriber) {
        if (subscriber == null) {
            new DefaultKafkaEventSubscriber() {

                @Override
                public void onEvent(Object event) {

                }
            }.subscribe(this);
        } else {
            subscriber.subscribe(this);
        }
    }
}
