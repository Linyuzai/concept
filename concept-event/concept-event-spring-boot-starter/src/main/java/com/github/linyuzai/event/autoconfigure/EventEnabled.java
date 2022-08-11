package com.github.linyuzai.event.autoconfigure;

import com.github.linyuzai.event.kafka.autoconfigure.KafkaEventAutoConfiguration;
import com.github.linyuzai.event.rabbitmq.autoconfigure.RabbitEventAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

/**
 * 事件启用标记
 * <p>
 * Kafka 或 RabbitMQ 等都是通过 spring.factories 自动加载
 * <p>
 * 并且同时需要满足 {@link EnableEventConcept}
 * <p>
 * 所以使用该类作为是否自动加载的先决条件
 */
@Configuration
@AutoConfigureBefore({KafkaEventAutoConfiguration.class, RabbitEventAutoConfiguration.class})
public class EventEnabled {
}
