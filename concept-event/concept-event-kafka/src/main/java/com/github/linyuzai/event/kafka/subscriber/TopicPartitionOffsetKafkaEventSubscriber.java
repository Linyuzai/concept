package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.TopicPartitionOffset;

/**
 * 基于 {@link TopicPartitionOffset} 的 Kafka 事件订阅器
 */
@Getter
public class TopicPartitionOffsetKafkaEventSubscriber extends AbstractKafkaEventSubscriber {

    private final TopicPartitionOffset[] topicPartitions;

    public TopicPartitionOffsetKafkaEventSubscriber(TopicPartitionOffset... topicPartitions) {
        this.topicPartitions = topicPartitions;
    }

    @Override
    public MessageListenerContainer createMessageListenerContainer(KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createContainer(topicPartitions);
    }
}
