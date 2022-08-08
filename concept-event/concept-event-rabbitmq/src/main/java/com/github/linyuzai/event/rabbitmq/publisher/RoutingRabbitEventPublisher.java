package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoutingRabbitEventPublisher extends AbstractRabbitEventPublisher {

    private final String exchange;

    private final String routingKey;

    @Override
    public void send(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        endpoint.getTemplate().convertAndSend(exchange, routingKey, event);
    }
}
