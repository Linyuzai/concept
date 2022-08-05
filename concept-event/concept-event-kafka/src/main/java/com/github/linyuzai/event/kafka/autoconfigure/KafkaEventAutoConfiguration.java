package com.github.linyuzai.event.kafka.autoconfigure;

import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointConfigurer;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointFactory;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpointFactoryImpl;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineConfigurer;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineFactory;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngineFactoryImpl;
import com.github.linyuzai.event.kafka.inherit.KafkaInheritHandler;
import com.github.linyuzai.event.kafka.inherit.KafkaInheritHandlerImpl;
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
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnClass(EnableKafka.class)
@ConditionalOnProperty(name = "concept.event.kafka.enabled", havingValue = "true")
@ConditionalOnBean(name = "com.github.linyuzai.event.autoconfigure.EventEnabled")
@EnableConfigurationProperties(KafkaEventProperties.class)
@AutoConfigureBefore(KafkaAutoConfiguration.class)
public class KafkaEventAutoConfiguration {

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>();
    }

    @Bean
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(Collections.emptyMap());
    }

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    /*@Bean
    @ConditionalOnMissingBean(ProducerListener.class)
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }*/

    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Collections.emptyMap());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(Collections.emptyMap());
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaInheritHandler kafkaInheritHandler(Environment environment) {
        return new KafkaInheritHandlerImpl(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaEventEngineFactory kafkaEventEngineFactory() {
        return new KafkaEventEngineFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaEventEndpointFactory kafkaEventEndpointFactory() {
        return new KafkaEventEndpointFactoryImpl();
    }

    @Bean
    public KafkaEventEngine kafkaEventEngine(ConfigurableBeanFactory beanFactory,
                                             KafkaEventProperties properties,
                                             KafkaInheritHandler inheritHandler,
                                             KafkaEventEngineFactory engineFactory,
                                             KafkaEventEndpointFactory endpointFactory,
                                             List<KafkaEventEngineConfigurer> engineConfigurers,
                                             List<KafkaEventEndpointConfigurer> endpointConfigurers) {
        inheritHandler.inherit(properties);

        KafkaEventEngine engine = engineFactory.create(properties);

        List<Map.Entry<String, KafkaEventProperties.ExtendedKafkaProperties>> entries =
                properties.getEndpoints()
                        .entrySet()
                        .stream()
                        .filter(it -> it.getValue().isEnabled())
                        .collect(Collectors.toList());

        for (Map.Entry<String, KafkaEventProperties.ExtendedKafkaProperties> entry : entries) {

            String key = entry.getKey();

            KafkaEventProperties.ExtendedKafkaProperties value = entry.getValue();

            KafkaEventEndpoint endpoint = endpointFactory.create(key, value, engine);

            for (KafkaEventEndpointConfigurer configurer : endpointConfigurers) {
                configurer.configure(endpoint);
            }

            engine.addEndpoints(endpoint);

            registerEndpoint(key, endpoint, beanFactory);
        }
        for (KafkaEventEngineConfigurer configurer : engineConfigurers) {
            configurer.configure(engine);
        }
        return engine;
    }

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
