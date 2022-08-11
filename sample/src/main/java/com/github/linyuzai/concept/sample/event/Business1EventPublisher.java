package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;

public class Business1EventPublisher implements EventPublisher {

    @Override
    public void publish(Object event, EventEndpoint endpoint, EventContext context) {
        if (endpoint.getName().equals("kafka1")) {
            //KafkaEventEndpoint持有KafkaTemplate
            KafkaTemplate<Object, Object> kafkaTemplate =
                    ((KafkaEventEndpoint) endpoint).getTemplate();
            kafkaTemplate.sendDefault(event);
        }
        if (endpoint.getName().equals("rabbitmq2")) {
            //RabbitEventEndpoint持有RabbitTemplate
            RabbitTemplate rabbitTemplate =
                    ((RabbitEventEndpoint) endpoint).getTemplate();
            rabbitTemplate.convertAndSend(event);
        }
    }
}
