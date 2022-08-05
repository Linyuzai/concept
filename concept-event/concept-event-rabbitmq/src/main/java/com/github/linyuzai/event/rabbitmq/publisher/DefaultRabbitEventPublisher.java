package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultRabbitEventPublisher extends RabbitEventPublisher {

    @Override
    public void publishRabbit(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        endpoint.getTemplate().convertAndSend(event);
    }
}
