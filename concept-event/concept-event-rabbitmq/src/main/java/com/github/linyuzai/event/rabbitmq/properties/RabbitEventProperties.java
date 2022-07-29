package com.github.linyuzai.event.rabbitmq.properties;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.concept.EventOperator;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties("concept.event.rabbitmq")
public class RabbitEventProperties implements EventOperator.PropertyConfig {

    private boolean enabled = true;

    private Map<Object, Object> metadata = new LinkedHashMap<>();

    private Class<? extends EventEncoder> encoder;

    private Class<? extends EventDecoder> decoder;

    private Class<? extends EventErrorHandler> errorHandler;

    private Class<? extends EventPublisher> publisher;

    private Class<? extends EventSubscriber> subscriber;

    private Map<String, ExtendedRabbitProperties> endpoints = new LinkedHashMap<>();

    @Getter
    @Setter
    public static class ExtendedRabbitProperties extends RabbitProperties implements EventOperator.PropertyConfig {

        private boolean enabled = true;

        private String inherit;

        private Map<Object, Object> metadata = new LinkedHashMap<>();

        private Class<? extends EventEncoder> encoder;

        private Class<? extends EventDecoder> decoder;

        private Class<? extends EventErrorHandler> errorHandler;

        private Class<? extends EventPublisher> publisher;

        private Class<? extends EventSubscriber> subscriber;
    }
}
