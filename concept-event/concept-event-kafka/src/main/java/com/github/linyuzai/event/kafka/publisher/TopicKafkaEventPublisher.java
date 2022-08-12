package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 给某个固定 Topic 发送消息的事件发布器
 */
@Getter
@AllArgsConstructor
public class TopicKafkaEventPublisher extends AbstractKafkaEventPublisher {

    /**
     * 指定的 Topic
     */
    private final String topic;

    @Override
    public ListenableFuture<SendResult<Object, Object>> send(Object event, KafkaEventEndpoint endpoint, EventContext context) {
        return endpoint.getTemplate().send(topic, event);
    }
}
