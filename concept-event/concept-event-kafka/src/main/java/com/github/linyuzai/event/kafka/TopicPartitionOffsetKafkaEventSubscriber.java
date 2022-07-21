package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.TopicPartitionOffset;

import java.lang.reflect.Type;

@Getter
public abstract class TopicPartitionOffsetKafkaEventSubscriber<T> extends DefaultKafkaEventSubscriber<T> {

    private final TopicPartitionOffset[] topicPartitions;

    public TopicPartitionOffsetKafkaEventSubscriber(TopicPartitionOffset... topicPartitions) {
        this.topicPartitions = topicPartitions;
    }

    @Override
    public MessageListenerContainer createContainer(Type type, KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topicPartitions);
    }
}
