/*
package com.github.linyuzai.concept.sample.event.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaOneConfig {

    @Value("${spring.kafka.one.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.one.consumer.group-id}")
    private String groupId;

    @Bean
    public KafkaTemplate<String, String> kafkaOneTemplate() {
        return new KafkaTemplate<>(producerOneFactory());
    }

    @Bean
    public KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaOneContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerOneFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, String> producerOneFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ConsumerFactory<String, String> consumerOneFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return props;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return props;
    }
}
*/
