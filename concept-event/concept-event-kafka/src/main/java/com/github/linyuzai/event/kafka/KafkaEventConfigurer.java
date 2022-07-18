package com.github.linyuzai.event.kafka;

public interface KafkaEventConfigurer {

    void configureEngine(KafkaEventEngine engine);

    void configureEndpoint(KafkaEventEndpoint endpoint);
}
