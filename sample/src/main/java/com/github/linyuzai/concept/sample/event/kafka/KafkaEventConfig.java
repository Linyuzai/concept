package com.github.linyuzai.concept.sample.event.kafka;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.codec.JacksonEventDecoder;
import com.github.linyuzai.event.core.codec.JacksonEventEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@EnableKafka
@Configuration
public class KafkaEventConfig {

    @Bean
    public EventEncoder eventEncoder() {
        return new JacksonEventEncoder();
    }

    @Bean
    public EventDecoder eventDecoder() {
        return new JacksonEventDecoder();
    }
}
