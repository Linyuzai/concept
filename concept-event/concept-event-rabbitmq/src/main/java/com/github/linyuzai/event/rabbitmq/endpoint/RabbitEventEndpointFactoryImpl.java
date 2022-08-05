package com.github.linyuzai.event.rabbitmq.endpoint;

import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class RabbitEventEndpointFactoryImpl implements RabbitEventEndpointFactory {

    private ResourceLoader resourceLoader;

    @Override
    public RabbitEventEndpoint create(String name,
                                      RabbitEventProperties.ExtendedRabbitProperties properties,
                                      RabbitEventEngine engine) {

        ConnectionFactory connectionFactory = createConnectionFactory(properties);

        RabbitListenerContainerFactory<? extends MessageListenerContainer> listenerContainerFactory = createRabbitListenerContainerFactory(properties, connectionFactory);

        RabbitTemplate rabbitTemplate = createRabbitTemplate(properties, connectionFactory);

        RabbitAdmin rabbitAdmin = createRabbitAdmin(connectionFactory);

        RabbitEventEndpoint endpoint = new RabbitEventEndpoint(name, engine);
        endpoint.setProperties(properties);
        endpoint.setConnectionFactory(connectionFactory);
        endpoint.setListenerContainerFactory(listenerContainerFactory);
        endpoint.setTemplate(rabbitTemplate);
        endpoint.setAdmin(rabbitAdmin);

        properties.apply(endpoint);

        return endpoint;
    }

    @SneakyThrows
    protected ConnectionFactory createConnectionFactory(RabbitProperties properties) {
        RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer =
                new RabbitConnectionFactoryBeanConfigurer(resourceLoader, properties);
        /*rabbitConnectionFactoryBeanConfigurer.setCredentialsProvider(credentialsProvider.getIfUnique());
        rabbitConnectionFactoryBeanConfigurer.setCredentialsRefreshService(credentialsRefreshService.getIfUnique());
        */
        CachingConnectionFactoryConfigurer rabbitCachingConnectionFactoryConfigurer =
                new CachingConnectionFactoryConfigurer(properties);
        /*rabbitCachingConnectionFactoryConfigurer.setConnectionNameStrategy(connectionNameStrategy.getIfUnique());
         */
        RabbitConnectionFactoryBean connectionFactoryBean = new RabbitConnectionFactoryBean();
        rabbitConnectionFactoryBeanConfigurer.configure(connectionFactoryBean);
        connectionFactoryBean.afterPropertiesSet();
        com.rabbitmq.client.ConnectionFactory connectionFactory = connectionFactoryBean.getObject();

        CachingConnectionFactory factory = new CachingConnectionFactory(Objects.requireNonNull(connectionFactory));
        rabbitCachingConnectionFactoryConfigurer.configure(factory);

        return factory;
    }

    protected RabbitListenerContainerFactory<? extends MessageListenerContainer> createRabbitListenerContainerFactory(
            RabbitProperties properties,
            ConnectionFactory connectionFactory) {
        RabbitProperties.ContainerType type = properties.getListener().getType();
        if (type == RabbitProperties.ContainerType.SIMPLE) {
            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            SimpleRabbitListenerContainerFactoryConfigurer simpleConfigurer =
                    new SimpleRabbitListenerContainerFactoryConfigurer(properties);
            //configureConfigurer(simpleConfigurer);
            simpleConfigurer.configure(factory, connectionFactory);
            return factory;
        }
        if (type == RabbitProperties.ContainerType.DIRECT) {
            DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
            DirectRabbitListenerContainerFactoryConfigurer directConfigurer = new DirectRabbitListenerContainerFactoryConfigurer(
                    properties);
            //configureConfigurer(directConfigurer);
            directConfigurer.configure(factory, connectionFactory);
            return factory;
        }
        return null;
    }

    protected RabbitTemplate createRabbitTemplate(RabbitProperties properties,
                                                  ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        RabbitTemplateConfigurer configurer = new RabbitTemplateConfigurer(properties);
        /*configurer.setMessageConverter(messageConverter.getIfUnique());*/
        /*configurer.setRetryTemplateCustomizers(retryTemplateCustomizers.orderedStream()
                .collect(Collectors.toList()));*/
        configurer.configure(template, connectionFactory);
        return template;
    }

    protected RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Deprecated
    @SneakyThrows
    protected void configureConfigurer(AbstractRabbitListenerContainerFactoryConfigurer<?> configurer) {
        Method setMessageConverter = ClassUtils.getMethodIfAvailable(
                AbstractRabbitListenerContainerFactoryConfigurer.class,
                "setMessageConverter",
                MessageConverter.class);
        if (setMessageConverter != null) {
            ReflectionUtils.makeAccessible(setMessageConverter);
            /*setMessageConverter.invoke(configurer, messageConverter.getIfUnique());*/
        }

        Method setMessageRecoverer = ClassUtils.getMethodIfAvailable(
                AbstractRabbitListenerContainerFactoryConfigurer.class,
                "setMessageRecoverer",
                MessageRecoverer.class);
        if (setMessageRecoverer != null) {
            ReflectionUtils.makeAccessible(setMessageRecoverer);
            /*setMessageRecoverer.invoke(configurer, messageRecoverer.getIfUnique());*/
        }

        Method setRetryTemplateCustomizers = ClassUtils.getMethodIfAvailable(
                AbstractRabbitListenerContainerFactoryConfigurer.class,
                "setRetryTemplateCustomizers",
                List.class);
        if (setRetryTemplateCustomizers != null) {
            ReflectionUtils.makeAccessible(setRetryTemplateCustomizers);
            /*setRetryTemplateCustomizers.invoke(configurer,
                    retryTemplateCustomizers.orderedStream().collect(Collectors.toList()));*/
        }
    }
}
