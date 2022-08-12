package com.github.linyuzai.event.rabbitmq.exchange;

import com.github.linyuzai.event.core.exchange.EndpointExchange;

import java.util.Collection;

/**
 * RabbitMQ 端点交换机
 * <p>
 * 定位 RabbitMQ 引擎下的一个或多个端点
 */
public class RabbitEndpointExchange extends EndpointExchange {

    public RabbitEndpointExchange(String... endpoints) {
        super(new RabbitEngineExchange(), endpoints);
    }

    public RabbitEndpointExchange(Collection<String> endpoints) {
        super(new RabbitEngineExchange(), endpoints);
    }
}
