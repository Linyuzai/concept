package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.endpoint.AbstractEventPublishEndpoint;
import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@CommonsLog
public class KafkaEventPublishEndpoint extends AbstractEventPublishEndpoint {

    private final EventPublisher DEFAULT_PUBLISHER = new EventPublisher() {
        @Override
        public void publish(Object event, EventPublishEndpoint endpoint) {
            ListenableFuture<SendResult<Object, Object>> send = template.sendDefault(event);
            send.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onFailure(@NonNull Throwable e) {
                    log.error("Event publish to kafka failure", e);
                }

                @Override
                public void onSuccess(SendResult<Object, Object> result) {

                }
            });
        }
    };

    private final EventSubscriber subscriber = new EventSubscriber() {
        @Override
        public void subscribe(EventPublishEndpoint endpoint) {
            if (endpoint instanceof KafkaEventPublishEndpoint) {
                KafkaListenerContainerFactory<? extends MessageListenerContainer> factory = ((KafkaEventPublishEndpoint) endpoint).getListenerContainerFactory();

                //factory.createListenerContainer().start();
            }
        }

        @Override
        public void onEvent() {

        }
    };

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

    public KafkaEventPublishEndpoint() {
        setDefaultPublisher(DEFAULT_PUBLISHER);
    }

    @Override
    public void subscribe(EventSubscriber subscriber) {
        subscriber.subscribe(this);
    }
}
