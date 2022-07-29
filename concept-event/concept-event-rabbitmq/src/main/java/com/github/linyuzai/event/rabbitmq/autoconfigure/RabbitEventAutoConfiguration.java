package com.github.linyuzai.event.rabbitmq.autoconfigure;

import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RabbitEventProperties.class)
public class RabbitEventAutoConfiguration {
}
