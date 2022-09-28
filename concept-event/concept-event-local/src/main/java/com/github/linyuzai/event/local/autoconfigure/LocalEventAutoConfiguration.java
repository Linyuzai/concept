package com.github.linyuzai.event.local.autoconfigure;

import com.github.linyuzai.event.core.config.EngineEndpointConfiguration;
import com.github.linyuzai.event.local.endpoint.LocalEventEndpoint;
import com.github.linyuzai.event.local.endpoint.LocalEventEndpointConfigurer;
import com.github.linyuzai.event.local.endpoint.LocalEventEndpointFactory;
import com.github.linyuzai.event.local.endpoint.LocalEventEndpointFactoryImpl;
import com.github.linyuzai.event.local.engine.LocalEventEngine;
import com.github.linyuzai.event.local.engine.LocalEventEngineConfigurer;
import com.github.linyuzai.event.local.engine.LocalEventEngineFactory;
import com.github.linyuzai.event.local.engine.LocalEventEngineFactoryImpl;
import com.github.linyuzai.event.local.inherit.LocalConfigInheritHandler;
import com.github.linyuzai.event.local.inherit.ReflectionLocalConfigInheritHandler;
import com.github.linyuzai.event.local.properties.LocalEventProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * 需要启用 @EnableEventConcept
 */
@Configuration
@ConditionalOnProperty(name = "concept.event.local.enabled", havingValue = "true")
@ConditionalOnBean(name = "com.github.linyuzai.event.autoconfigure.EventEnabled")
@EnableConfigurationProperties(LocalEventProperties.class)
public class LocalEventAutoConfiguration extends EngineEndpointConfiguration<LocalEventProperties,
        LocalEventProperties.LocalProperties, LocalEventEngine, LocalEventEndpoint> {

    /**
     * 本地配置继承处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalConfigInheritHandler localInheritHandler(Environment environment) {
        return new ReflectionLocalConfigInheritHandler(environment);
    }

    /**
     * 本地事件引擎工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalEventEngineFactory localEventEngineFactory() {
        return new LocalEventEngineFactoryImpl();
    }

    /**
     * 本地事件端点工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalEventEndpointFactory localEventEndpointFactory() {
        return new LocalEventEndpointFactoryImpl();
    }

    /**
     * 创建本地事件引擎和本地事件端点
     */
    @Bean
    public LocalEventEngine localEventEngine(ConfigurableBeanFactory beanFactory,
                                             LocalEventProperties properties,
                                             LocalConfigInheritHandler inheritHandler,
                                             LocalEventEngineFactory engineFactory,
                                             LocalEventEndpointFactory endpointFactory,
                                             List<LocalEventEngineConfigurer> engineConfigurers,
                                             List<LocalEventEndpointConfigurer> endpointConfigurers) {
        return configure(properties, inheritHandler, engineFactory, endpointFactory,
                engineConfigurers, endpointConfigurers, (name, endpoint) ->
                        registerEndpoint(name, endpoint, beanFactory));
    }

    /**
     * 注册本地事件端点
     */
    private void registerEndpoint(String name, LocalEventEndpoint endpoint, ConfigurableBeanFactory beanFactory) {
        register(name + "LocalEventEndpoint", endpoint, beanFactory);
    }

    private void register(String name, Object bean, ConfigurableBeanFactory beanFactory) {
        if (bean == null) {
            return;
        }
        beanFactory.registerSingleton(name, bean);
    }
}
