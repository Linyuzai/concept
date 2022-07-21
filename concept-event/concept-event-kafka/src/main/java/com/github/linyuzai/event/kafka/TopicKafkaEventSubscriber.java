package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.lang.reflect.Type;

@Getter
public abstract class TopicKafkaEventSubscriber<T> extends DefaultKafkaEventSubscriber<T> {

    private final String[] topics;

    public TopicKafkaEventSubscriber(String... topics) {
        this.topics = topics;
    }

    @Override
    public MessageListenerContainer createContainer(Type type, KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topics);
    }
}
