package com.github.linyuzai.event.kafka.endpoint;

public interface KafkaEventEndpointConfigurer {

    void configure(KafkaEventEndpoint endpoint);
}
