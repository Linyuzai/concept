package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 默认的 Kafka 事件发布器
 * <p>
 * 调用 {@link org.springframework.kafka.core.KafkaTemplate#sendDefault(Object)} 发送消息
 */
public class DefaultKafkaEventPublisher extends AbstractKafkaEventPublisher {

    @Override
    public ListenableFuture<SendResult<Object, Object>> send(Object event, KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getTemplate().sendDefault(event);
    }
}
