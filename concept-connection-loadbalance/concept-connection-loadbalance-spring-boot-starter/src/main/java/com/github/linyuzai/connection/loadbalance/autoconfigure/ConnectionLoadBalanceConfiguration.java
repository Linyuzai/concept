package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.logger.ErrorLogger;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.repository.DefaultConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.core.monitor.LoadBalanceMonitorLogger;
import com.github.linyuzai.connection.loadbalance.core.extension.ScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.extension.SingleThreadScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.discovery.DiscoveryConnectionServerProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ConnectionLoadBalanceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionRepository connectionRepository() {
        return new DefaultConnectionRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({DiscoveryClient.class, Registration.class})
    public ConnectionServerProvider connectionServerProvider(DiscoveryClient client, Registration registration) {
        return new DiscoveryConnectionServerProvider(client, registration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorHandler errorHandler() {
        Log log = LogFactory.getLog(ErrorLogger.class);
        return new ErrorLogger(log::info, log::error);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionEventPublisher connectionEventPublisher(ApplicationEventPublisher publisher) {
        return new ApplicationConnectionEventPublisher(publisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduledExecutorServiceFactory scheduledExecutorServiceFactory() {
        return new SingleThreadScheduledExecutorServiceFactory();
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance",
            name = "logger", havingValue = "true", matchIfMissing = true)
    public ConnectionSubscribeLogger connectionSubscribeLogger() {
        Log log = LogFactory.getLog(ConnectionSubscribeLogger.class);
        return new ConnectionSubscribeLogger(log::info, log::error);
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance.monitor",
            name = "logger", havingValue = "true", matchIfMissing = true)
    public LoadBalanceMonitorLogger loadBalanceMonitorLogger() {
        Log log = LogFactory.getLog(LoadBalanceMonitorLogger.class);
        return new LoadBalanceMonitorLogger(log::info, log::error);
    }
}
