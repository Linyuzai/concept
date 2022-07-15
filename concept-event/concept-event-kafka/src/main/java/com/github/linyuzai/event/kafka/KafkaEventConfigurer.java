package com.github.linyuzai.event.kafka;

public interface KafkaEventConfigurer {

    void configureEngine(KafkaEventPublishEngine engine);

    void configureEndpoint(KafkaEventPublishEndpoint endpoint);
}
