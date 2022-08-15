package com.github.linyuzai.concept.sample.event.rabbitmq.custom;

import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineConfigurer;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CustomRabbitEventEngineConfigurer implements RabbitEventEngineConfigurer {

    @Override
    public void configure(RabbitEventEngine engine) {
        //自定义配置
    }
}
