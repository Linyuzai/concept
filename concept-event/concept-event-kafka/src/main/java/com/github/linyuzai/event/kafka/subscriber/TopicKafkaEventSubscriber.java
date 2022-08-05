package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

@Getter
public class TopicKafkaEventSubscriber extends DefaultKafkaEventSubscriber {

    private final String[] topics;

    public TopicKafkaEventSubscriber(String... topics) {
        this.topics = topics;
    }

    @Override
    public MessageListenerContainer createMessageListenerContainer(KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topics);
    }
}
