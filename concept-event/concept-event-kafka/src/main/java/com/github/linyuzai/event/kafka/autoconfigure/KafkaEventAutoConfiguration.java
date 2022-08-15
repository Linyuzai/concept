package com.github.linyuzai.event.kafka.autoconfigure;

import com.github.linyuzai.event.core.config.EngineEndpointConfiguration;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointConfigurer;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointFactory;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointFactoryImpl;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineConfigurer;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineFactory;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineFactoryImpl;
import com.github.linyuzai.event.kafka.inherit.KafkaConfigInheritHandler;
import com.github.linyuzai.event.kafka.inherit.ReflectionKafkaConfigInheritHandler;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.Collections;
import java.util.List;

/**
 * 需要集成 kafka 模块并启用 @EnableEventConcept
 * <p>
 * 替换 {@link KafkaAutoConfiguration} 中配置的 Bean
 * <p>
 * 这些替换的 Bean 无法使用仅仅用于不让默认的配置生效
 */
@Configuration
@ConditionalOnClass(EnableKafka.class)
@ConditionalOnProperty(name = "concept.event.kafka.enabled", havingValue = "true")
@ConditionalOnBean(name = "com.github.linyuzai.event.autoconfigure.EventEnabled")
@EnableConfigurationProperties(KafkaEventProperties.class)
@AutoConfigureBefore(KafkaAutoConfiguration.class)
public class KafkaEventAutoConfiguration extends EngineEndpointConfiguration<KafkaEventProperties,
        KafkaEventProperties.ExtendedKafkaProperties, KafkaEventEngine, KafkaEventEndpoint> {

    /**
     * 覆盖默认配置
     */
    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>();
    }

    /**
     * 覆盖默认配置
     */
    @Bean
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(Collections.emptyMap());
    }

    /**
     * 覆盖默认配置
     */
    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    /*@Bean
    @ConditionalOnMissingBean(ProducerListener.class)
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }*/

    /**
     * 覆盖默认配置
     */
    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Collections.emptyMap());
    }

    /**
     * 覆盖默认配置
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(Collections.emptyMap());
    }

    /**
     * Kafka 配置继承处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaConfigInheritHandler kafkaInheritHandler(Environment environment) {
        return new ReflectionKafkaConfigInheritHandler(environment);
    }

    /**
     * Kafka 事件引擎工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaEventEngineFactory kafkaEventEngineFactory() {
        return new KafkaEventEngineFactoryImpl();
    }

    /**
     * Kafka 事件端点工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaEventEndpointFactory kafkaEventEndpointFactory() {
        return new KafkaEventEndpointFactoryImpl();
    }

    /**
     * 创建 Kafka 事件引擎和 Kafka 事件端点
     */
    @Bean
    public KafkaEventEngine kafkaEventEngine(ConfigurableBeanFactory beanFactory,
                                             KafkaEventProperties properties,
                                             KafkaConfigInheritHandler inheritHandler,
                                             KafkaEventEngineFactory engineFactory,
                                             KafkaEventEndpointFactory endpointFactory,
                                             List<KafkaEventEngineConfigurer> engineConfigurers,
                                             List<KafkaEventEndpointConfigurer> endpointConfigurers) {
        return configure(properties, inheritHandler, engineFactory, endpointFactory,
                engineConfigurers, endpointConfigurers, (name, endpoint) ->
                        registerEndpoint(name, endpoint, beanFactory));
    }

    /**
     * 注册 Kafka 事件端点
     */
    private void registerEndpoint(String name, KafkaEventEndpoint endpoint, ConfigurableBeanFactory beanFactory) {
        register(name + "KafkaProducerFactory", endpoint.getProducerFactory(), beanFactory);
        register(name + "KafkaProducerListener", endpoint.getProducerListener(), beanFactory);
        register(name + "KafkaTemplate", endpoint.getTemplate(), beanFactory);
        register(name + "KafkaConsumerFactory", endpoint.getConsumerFactory(), beanFactory);
        register(name + "KafkaTransactionManager", endpoint.getTransactionManager(), beanFactory);
        register(name + "KafkaListenerContainerFactory", endpoint.getListenerContainerFactory(), beanFactory);
        register(name + "KafkaAdmin", endpoint.getAdmin(), beanFactory);
        register(name + "KafkaEventEndpoint", endpoint, beanFactory);
    }

    private void register(String name, Object bean, ConfigurableBeanFactory beanFactory) {
        if (bean == null) {
            return;
        }
        beanFactory.registerSingleton(name, bean);
    }
}
