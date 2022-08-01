package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class TopicPatternKafkaEventSubscriber extends DefaultKafkaEventSubscriber {

    private final Pattern topicPattern;

    @Override
    public MessageListenerContainer createContainer(KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topicPattern);
    }
}
