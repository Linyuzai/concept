package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

/**
 * 指定 Topic 的 Kafka 事件订阅器
 */
@Getter
public class TopicKafkaEventSubscriber extends AbstractKafkaEventSubscriber {

    /**
     * 指定的 Topic
     */
    private final String[] topics;

    public TopicKafkaEventSubscriber(String... topics) {
        this.topics = topics;
    }

    @Override
    public MessageListenerContainer createMessageListenerContainer(KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topics);
    }
}
