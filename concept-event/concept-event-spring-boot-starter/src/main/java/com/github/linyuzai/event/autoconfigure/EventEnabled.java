package com.github.linyuzai.event.autoconfigure;

import com.github.linyuzai.event.kafka.KafkaEventAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(KafkaEventAutoConfiguration.class)
public class EventEnabled {
}
