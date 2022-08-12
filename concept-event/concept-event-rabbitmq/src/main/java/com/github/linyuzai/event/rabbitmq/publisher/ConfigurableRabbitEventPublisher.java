package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.util.function.Supplier;

/**
 * 可配置的 RabbitMQ 事件发布器
 */
@Setter
@Getter
public class ConfigurableRabbitEventPublisher extends AbstractRabbitEventPublisher {

    private Supplier<String> exchange = () -> null;

    private Supplier<String> routingKey = () -> null;

    private Supplier<CorrelationData> correlationData = () -> null;

    @Override
    public void send(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        endpoint.getTemplate().convertAndSend(exchange.get(), routingKey.get(), event, correlationData.get());
    }
}
