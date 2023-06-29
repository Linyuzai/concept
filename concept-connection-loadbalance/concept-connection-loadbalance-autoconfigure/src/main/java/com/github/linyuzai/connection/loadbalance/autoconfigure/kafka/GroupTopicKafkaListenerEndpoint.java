package com.github.linyuzai.connection.loadbalance.autoconfigure.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.kafka.support.converter.MessageConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public class GroupTopicKafkaListenerEndpoint implements KafkaListenerEndpoint {

    private final String groupId;

    private final String topic;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public Collection<String> getTopics() {
        return Collections.singletonList(topic);
    }

    @Override
    public TopicPartitionOffset[] getTopicPartitionsToAssign() {
        return new TopicPartitionOffset[0];
    }

    @Override
    public Pattern getTopicPattern() {
        return null;
    }

    @Override
    public String getClientIdPrefix() {
        return null;
    }

    @Override
    public Integer getConcurrency() {
        return null;
    }

    @Override
    public Boolean getAutoStartup() {
        return null;
    }

    @Override
    public void setupListenerContainer(MessageListenerContainer listenerContainer, MessageConverter messageConverter) {

    }

    @Override
    public boolean isSplitIterables() {
        return false;
    }
}
