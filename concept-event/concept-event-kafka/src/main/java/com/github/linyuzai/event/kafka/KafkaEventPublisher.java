package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.publisher.AbstractEventPublisher;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@AllArgsConstructor
public class KafkaEventPublisher extends AbstractEventPublisher {

    private KafkaTemplate<Object, Object> template;

    @Override
    public void publish(Object event) {
        ProducerRecord<Object, Object> record = new ProducerRecord<>("", event);
        ListenableFuture<SendResult<Object, Object>> send = template.send(record);

    }
}
