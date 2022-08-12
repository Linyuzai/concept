package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListenerContainer;

/**
 * 基于 {@link KafkaListenerEndpoint} 创建消息监听器容器的 Kafka 事件订阅器
 */
@Getter
@AllArgsConstructor
public class DefaultKafkaEventSubscriber extends AbstractKafkaEventSubscriber {

    private final KafkaListenerEndpoint listenerEndpoint;

    @Override
    public MessageListenerContainer createMessageListenerContainer(KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getListenerContainerFactory().createListenerContainer(listenerEndpoint);
    }
}
