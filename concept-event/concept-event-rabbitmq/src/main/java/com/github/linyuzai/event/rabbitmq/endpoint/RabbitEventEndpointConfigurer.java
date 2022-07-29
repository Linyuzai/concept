package com.github.linyuzai.event.rabbitmq.endpoint;

public interface RabbitEventEndpointConfigurer {

    void configure(RabbitEventEndpoint endpoint);
}
