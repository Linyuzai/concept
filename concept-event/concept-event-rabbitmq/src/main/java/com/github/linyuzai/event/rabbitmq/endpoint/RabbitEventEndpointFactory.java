package com.github.linyuzai.event.rabbitmq.endpoint;

import com.github.linyuzai.event.core.endpoint.EventEndpointFactory;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

public interface RabbitEventEndpointFactory extends
        EventEndpointFactory<RabbitEventProperties.ExtendedRabbitProperties, RabbitEventEngine, RabbitEventEndpoint> {
}
