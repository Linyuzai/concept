package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.function.Supplier;

/**
 * 可配置的 Kafka 事件发布器
 */
@Setter
@Getter
public class ConfigurableKafkaEventPublisher extends AbstractKafkaEventPublisher {

    private Supplier<String> topic = () -> null;

    private Supplier<Integer> partition = () -> null;

    private Supplier<Long> timestamp = () -> null;

    private Supplier<Object> key = () -> null;

    @Override
    public ListenableFuture<SendResult<Object, Object>> send(Object event, KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getTemplate().send(topic.get(), partition.get(), timestamp.get(), key.get(), event);
    }
}
