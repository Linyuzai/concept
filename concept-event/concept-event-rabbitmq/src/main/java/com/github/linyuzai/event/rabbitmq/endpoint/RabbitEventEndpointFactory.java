package com.github.linyuzai.event.rabbitmq.endpoint;

import com.github.linyuzai.event.core.endpoint.EventEndpointFactory;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

/**
 * RabbitMQ 事件端点工厂
 */
public interface RabbitEventEndpointFactory extends
        EventEndpointFactory<RabbitEventProperties.ExtendedRabbitProperties, RabbitEventEngine, RabbitEventEndpoint> {
}
