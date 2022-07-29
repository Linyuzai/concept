package com.github.linyuzai.event.autoconfigure;

import com.github.linyuzai.event.kafka.autoconfigure.KafkaEventAutoConfiguration;
import com.github.linyuzai.event.rabbitmq.autoconfigure.RabbitEventAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore({KafkaEventAutoConfiguration.class, RabbitEventAutoConfiguration.class})
public class EventEnabled {
}
