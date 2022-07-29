package com.github.linyuzai.event.rabbitmq.exchange;

import com.github.linyuzai.event.core.exchange.EndpointExchange;

import java.util.Collection;

public class RabbitEndpointExchange extends EndpointExchange {

    public RabbitEndpointExchange(String... endpoints) {
        super(new RabbitEngineExchange(), endpoints);
    }

    public RabbitEndpointExchange(Collection<String> endpoints) {
        super(new RabbitEngineExchange(), endpoints);
    }
}
