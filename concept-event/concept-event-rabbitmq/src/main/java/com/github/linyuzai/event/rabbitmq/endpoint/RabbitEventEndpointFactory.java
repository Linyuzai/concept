package com.github.linyuzai.event.rabbitmq.endpoint;

import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

public interface RabbitEventEndpointFactory {

    RabbitEventEndpoint create(String name, RabbitEventProperties.ExtendedRabbitProperties properties);
}
