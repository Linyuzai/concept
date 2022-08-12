package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基于 交换机 和 路由键 发布消息
 */
@Getter
@AllArgsConstructor
public class RoutingRabbitEventPublisher extends AbstractRabbitEventPublisher {

    /**
     * 指定交换机
     */
    private final String exchange;

    /**
     * 指定路由键
     */
    private final String routingKey;

    @Override
    public void send(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        endpoint.getTemplate().convertAndSend(exchange, routingKey, event);
    }
}
