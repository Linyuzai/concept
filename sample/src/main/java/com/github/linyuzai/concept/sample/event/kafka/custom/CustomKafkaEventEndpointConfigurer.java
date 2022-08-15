package com.github.linyuzai.concept.sample.event.kafka.custom;

import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointConfigurer;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CustomKafkaEventEndpointConfigurer implements KafkaEventEndpointConfigurer {

    @Override
    public void configure(KafkaEventEndpoint endpoint) {
        //自定义配置
        //endpoint.setPublisher();
        //endpoint.setSubscriber();
        //endpoint.setErrorHandler();
    }
}
