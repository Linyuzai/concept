package com.github.linyuzai.event.rabbitmq.autoconfigure;

import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointConfigurer;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointFactory;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointFactoryImpl;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineConfigurer;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineFactory;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineFactoryImpl;
import com.github.linyuzai.event.rabbitmq.inherit.RabbitInheritHandler;
import com.github.linyuzai.event.rabbitmq.inherit.RabbitInheritHandlerImpl;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(name = "concept.event.rabbitmq.enabled", havingValue = "true")
@ConditionalOnBean(name = "com.github.linyuzai.event.autoconfigure.EventEnabled")
@EnableConfigurationProperties(RabbitEventProperties.class)
@AutoConfigureBefore(RabbitAutoConfiguration.class)
public class RabbitEventAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RabbitInheritHandler rabbitInheritHandler() {
        return new RabbitInheritHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitEventEngineFactory rabbitEventEngineFactory() {
        return new RabbitEventEngineFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitEventEndpointFactory rabbitEventEndpointFactory() {
        return new RabbitEventEndpointFactoryImpl();
    }

    @Bean
    public RabbitEventEngine rabbitEventEngine(ConfigurableBeanFactory beanFactory,
                                               RabbitEventProperties properties,
                                               RabbitInheritHandler inheritHandler,
                                               RabbitEventEngineFactory engineFactory,
                                               RabbitEventEndpointFactory endpointFactory,
                                               List<RabbitEventEngineConfigurer> engineConfigurers,
                                               List<RabbitEventEndpointConfigurer> endpointConfigurers) {
        inheritHandler.inherit(properties);

        RabbitEventEngine engine = engineFactory.create(properties);

        List<Map.Entry<String, RabbitEventProperties.ExtendedRabbitProperties>> entries =
                properties.getEndpoints()
                        .entrySet()
                        .stream()
                        .filter(it -> it.getValue().isEnabled())
                        .collect(Collectors.toList());

        for (Map.Entry<String, RabbitEventProperties.ExtendedRabbitProperties> entry : entries) {

            String key = entry.getKey();

            RabbitEventProperties.ExtendedRabbitProperties value = entry.getValue();

            RabbitEventEndpoint endpoint = endpointFactory.create(key, value);

            for (RabbitEventEndpointConfigurer configurer : endpointConfigurers) {
                configurer.configure(endpoint);
            }

            engine.addEndpoints(endpoint);

            registerEndpoint(key, endpoint, beanFactory);
        }
        for (RabbitEventEngineConfigurer configurer : engineConfigurers) {
            configurer.configure(engine);
        }
        return engine;
    }

    private void registerEndpoint(String name, RabbitEventEndpoint endpoint, ConfigurableBeanFactory beanFactory) {

    }

    private void register(String name, Object bean, ConfigurableBeanFactory beanFactory) {
        if (bean == null) {
            return;
        }
        beanFactory.registerSingleton(name, bean);
    }
}
