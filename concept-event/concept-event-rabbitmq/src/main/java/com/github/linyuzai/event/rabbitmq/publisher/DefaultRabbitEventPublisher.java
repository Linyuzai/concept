package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import lombok.AllArgsConstructor;

/**
 * 默认的 RabbitMQ 事件发布器
 * <p>
 * 调用 {@link org.springframework.amqp.rabbit.core.RabbitTemplate#convertAndSend(Object)} 发送消息
 */
@AllArgsConstructor
public class DefaultRabbitEventPublisher extends AbstractRabbitEventPublisher {

    @Override
    public void send(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        endpoint.getTemplate().convertAndSend(event);
    }
}
