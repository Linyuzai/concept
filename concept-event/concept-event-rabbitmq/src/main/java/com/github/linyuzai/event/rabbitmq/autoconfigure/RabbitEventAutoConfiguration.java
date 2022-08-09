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
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

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
    public SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer() {
        return new SimpleRabbitListenerContainerFactoryConfigurer(new RabbitProperties());
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory() {
        return new SimpleRabbitListenerContainerFactory();
    }

    @Bean
    public DirectRabbitListenerContainerFactoryConfigurer directRabbitListenerContainerFactoryConfigurer() {
        return new DirectRabbitListenerContainerFactoryConfigurer(new RabbitProperties());
    }

    @Bean
    public RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer() {
        return new RabbitConnectionFactoryBeanConfigurer(null, null);
    }

    @Bean
    public CachingConnectionFactoryConfigurer rabbitConnectionFactoryConfigurer() {
        return new CachingConnectionFactoryConfigurer(new RabbitProperties());
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        return new CachingConnectionFactory(new com.rabbitmq.client.ConnectionFactory());
    }

    @Bean
    public RabbitTemplateConfigurer rabbitTemplateConfigurer() {
        return new RabbitTemplateConfigurer(new RabbitProperties());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(rabbitConnectionFactory());
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(rabbitConnectionFactory());
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitInheritHandler rabbitInheritHandler(Environment environment) {
        return new RabbitInheritHandlerImpl(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitEventEngineFactory rabbitEventEngineFactory() {
        return new RabbitEventEngineFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitEventEndpointFactory rabbitEventEndpointFactory(ResourceLoader resourceLoader) {
        return new RabbitEventEndpointFactoryImpl(resourceLoader);
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

            RabbitEventEndpoint endpoint = endpointFactory.create(key, value, engine);

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
        register(name + "RabbitConnectionFactory", endpoint.getConnectionFactory(), beanFactory);
        register(name + "RabbitListenerContainerFactory", endpoint.getListenerContainerFactory(), beanFactory);
        register(name + "RabbitTemplate", endpoint.getTemplate(), beanFactory);
        register(name + "RabbitAdmin", endpoint.getAdmin(), beanFactory);
    }

    private void register(String name, Object bean, ConfigurableBeanFactory beanFactory) {
        if (bean == null) {
            return;
        }
        beanFactory.registerSingleton(name, bean);
    }
}
