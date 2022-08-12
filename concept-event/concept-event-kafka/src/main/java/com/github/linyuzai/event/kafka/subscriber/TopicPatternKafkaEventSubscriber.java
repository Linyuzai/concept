package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.regex.Pattern;

/**
 * 基于 {@link Pattern} 的 Kafka Topic 事件订阅器
 */
@Getter
@AllArgsConstructor
public class TopicPatternKafkaEventSubscriber extends AbstractKafkaEventSubscriber {

    /**
     * Topic Pattern
     */
    private final Pattern topicPattern;

    @Override
    public MessageListenerContainer createMessageListenerContainer(KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topicPattern);
    }
}
