package com.github.linyuzai.concept.sample.event.kafka.custom;

import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineFactory;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import org.springframework.stereotype.Component;

//@Component
public class CustomKafkaEventEngineFactory implements KafkaEventEngineFactory {

    @Override
    public KafkaEventEngine create(KafkaEventProperties config) {
        //自定义
        return null;
    }
}
