package com.github.linyuzai.concept.sample.event.kafka.custom;

import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineConfigurer;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CustomKafkaEventEngineConfigurer implements KafkaEventEngineConfigurer {

    @Override
    public void configure(KafkaEventEngine engine) {
        //自定义配置
        //engine.setPublisher();
        //engine.setSubscriber();
    }
}
