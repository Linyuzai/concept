package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public abstract class TopicPatternKafkaEventSubscriber<T> extends DefaultKafkaEventSubscriber<T> {

    private final Pattern topicPattern;

    @Override
    public MessageListenerContainer createContainer(Type type, KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topicPattern);
    }
}
