package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@Getter
@AllArgsConstructor
public class TopicKafkaEventPublisher extends AbstractKafkaEventPublisher {

    private final String topic;

    @Override
    public ListenableFuture<SendResult<Object, Object>> send(Object event, KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getTemplate().send(topic, event);
    }
}
