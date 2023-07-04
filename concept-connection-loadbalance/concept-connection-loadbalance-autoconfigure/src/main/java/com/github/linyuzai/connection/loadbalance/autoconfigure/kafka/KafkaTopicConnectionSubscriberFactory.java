package com.github.linyuzai.connection.loadbalance.autoconfigure.kafka;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;

@Setter
@Getter
public class KafkaTopicConnectionSubscriberFactory extends MasterSlaveConnectionSubscriberFactory {

    private KafkaTemplate<?, Object> kafkaTemplate;

    private KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaListenerContainerFactory;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new KafkaTopicConnectionSubscriber(kafkaTemplate, kafkaListenerContainerFactory);
    }
}
