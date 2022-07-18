package com.github.linyuzai.event.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

@Getter
@AllArgsConstructor
public abstract class TopicKafkaEventSubscriber extends AbstractKafkaEventSubscriber {

    private String[] topics;

    @Override
    public MessageListenerContainer createContainer(KafkaEventEndpoint endpoint) {
        return endpoint.getListenerContainerFactory().createContainer(topics);
    }

    public abstract void onEvent(Object event);
}
