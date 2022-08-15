package com.github.linyuzai.concept.sample.event.kafka.custom;

import com.github.linyuzai.event.kafka.inherit.KafkaConfigInheritHandler;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import org.springframework.stereotype.Component;

//@Component
public class CustomKafkaConfigInheritHandler implements KafkaConfigInheritHandler {

    @Override
    public void inherit(KafkaEventProperties config) {

    }
}
