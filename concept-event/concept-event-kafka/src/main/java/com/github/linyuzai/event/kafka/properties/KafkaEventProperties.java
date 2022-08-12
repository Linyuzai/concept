package com.github.linyuzai.event.kafka.properties;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.config.AbstractPropertiesConfig;
import com.github.linyuzai.event.core.config.EndpointConfig;
import com.github.linyuzai.event.core.config.EngineConfig;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Kafka 事件配置
 */
@Getter
@Setter
@ConfigurationProperties("concept.event.kafka")
public class KafkaEventProperties extends AbstractPropertiesConfig implements EngineConfig {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * Kafka 事件端点配置
     */
    private Map<String, ExtendedKafkaProperties> endpoints = new LinkedHashMap<>();

    /**
     * Kafka 扩展配置
     */
    @Getter
    @Setter
    public static class ExtendedKafkaProperties extends KafkaProperties implements EndpointConfig {

        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 配置继承的端点名
         */
        private String inherit;

        /**
         * 元数据
         */
        private Map<Object, Object> metadata = new LinkedHashMap<>();

        /**
         * 事件编码器
         */
        private Class<? extends EventEncoder> encoder;

        /**
         * 事件解码器
         */
        private Class<? extends EventDecoder> decoder;

        /**
         * 事件异常处理器
         */
        private Class<? extends EventErrorHandler> errorHandler;

        /**
         * 事件发布器
         */
        private Class<? extends EventPublisher> publisher;

        /**
         * 事件订阅器
         */
        private Class<? extends EventSubscriber> subscriber;
    }
}
