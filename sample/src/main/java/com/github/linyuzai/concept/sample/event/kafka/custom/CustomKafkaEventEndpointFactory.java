package com.github.linyuzai.concept.sample.event.kafka.custom;

import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointFactory;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import org.springframework.stereotype.Component;

//@Component
public class CustomKafkaEventEndpointFactory implements KafkaEventEndpointFactory {

    @Override
    public KafkaEventEndpoint create(String name, KafkaEventProperties.ExtendedKafkaProperties config, KafkaEventEngine engine) {
        //自定义
        return null;
    }
}
