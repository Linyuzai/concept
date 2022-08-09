package com.github.linyuzai.event.kafka.properties;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.config.AbstractPropertiesConfig;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.inherit.Inheritable;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import com.github.linyuzai.event.core.config.PropertiesConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties("concept.event.kafka")
public class KafkaEventProperties extends AbstractPropertiesConfig implements PropertiesConfig, Inheritable {

    private boolean enabled;

    private Map<String, ExtendedKafkaProperties> endpoints = new LinkedHashMap<>();

    @Getter
    @Setter
    public static class ExtendedKafkaProperties extends KafkaProperties
            implements PropertiesConfig, Inheritable.Endpoint {

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
