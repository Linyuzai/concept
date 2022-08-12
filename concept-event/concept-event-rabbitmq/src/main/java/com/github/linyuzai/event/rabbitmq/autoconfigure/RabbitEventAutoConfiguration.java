package com.github.linyuzai.event.rabbitmq.autoconfigure;

import com.github.linyuzai.event.core.config.EngineEndpointConfiguration;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointConfigurer;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointFactory;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointFactoryImpl;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineConfigurer;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineFactory;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineFactoryImpl;
import com.github.linyuzai.event.rabbitmq.inherit.RabbitInheritHandler;
import com.github.linyuzai.event.rabbitmq.inherit.ReflectionRabbitInheritHandler;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelCallback;
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
import org.springframework.lang.NonNull;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = "concept.event.rabbitmq.enabled", havingValue = "true")
@ConditionalOnBean(name = "com.github.linyuzai.event.autoconfigure.EventEnabled")
@EnableConfigurationProperties(RabbitEventProperties.class)
@AutoConfigureBefore(RabbitAutoConfiguration.class)
public class RabbitEventAutoConfiguration extends EngineEndpointConfiguration<RabbitEventProperties,
        RabbitEventProperties.ExtendedRabbitProperties, RabbitEventEngine, RabbitEventEndpoint> {

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
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
        factory.setAutomaticRecoveryEnabled(false);
        return new CachingConnectionFactory(factory);
    }

    @Bean
    public RabbitTemplateConfigurer rabbitTemplateConfigurer() {
        return new RabbitTemplateConfigurer(new RabbitProperties());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(rabbitConnectionFactory()) {

            @SuppressWarnings("unchecked")
            @Override
            public <T> T execute(@NonNull ChannelCallback<T> action) {
                //RabbitHealthIndicator报错问题
                return (T) "";
            }
        };
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(rabbitConnectionFactory());
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitInheritHandler rabbitInheritHandler(Environment environment) {
        return new ReflectionRabbitInheritHandler(environment);
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
        return configure(properties, inheritHandler, engineFactory, endpointFactory,
                engineConfigurers, endpointConfigurers, (name, endpoint) ->
                        registerEndpoint(name, endpoint, beanFactory));
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
