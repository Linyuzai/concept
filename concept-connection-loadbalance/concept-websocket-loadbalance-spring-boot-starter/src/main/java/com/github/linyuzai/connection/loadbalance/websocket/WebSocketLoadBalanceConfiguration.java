package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.scope.ScopeHelper;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.extension.ScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.monitor.ConnectionLoadBalanceMonitor;
import com.github.linyuzai.connection.loadbalance.core.monitor.LoadBalanceMonitorLogger;
import com.github.linyuzai.connection.loadbalance.core.monitor.ScheduledConnectionLoadBalanceMonitor;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.repository.DefaultConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Bean
    public WebSocketScopeHelper webSocketScopeHelper(ScopeHelper helper) {
        return new WebSocketScopeHelper(helper);
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public ConnectionRepository connectionRepository() {
        return new DefaultConnectionRepository();
    }

    @Bean
    @WebSocketScope
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance",
            name = "logger", havingValue = "true", matchIfMissing = true)
    public ConnectionSubscribeLogger connectionSubscribeLogger() {
        Log log = LogFactory.getLog(ConnectionSubscribeLogger.class);
        return new ConnectionSubscribeLogger(log::info, log::error);
    }

    @Bean
    @WebSocketScope
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance.monitor",
            name = "logger", havingValue = "true", matchIfMissing = true)
    public LoadBalanceMonitorLogger loadBalanceMonitorLogger() {
        Log log = LogFactory.getLog(LoadBalanceMonitorLogger.class);
        return new LoadBalanceMonitorLogger(log::info, log::error);
    }

    @Bean
    @WebSocketScope
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance.monitor",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ScheduledConnectionLoadBalanceMonitor scheduledConnectionLoadBalanceMonitor(
            WebSocketLoadBalanceProperties properties,
            WebSocketScopeHelper helper) {
        ScheduledExecutorServiceFactory factory = helper.getBean(ScheduledExecutorServiceFactory.class);
        return new ScheduledConnectionLoadBalanceMonitor(
                factory.create(ConnectionLoadBalanceMonitor.class),
                properties.getLoadBalance().getMonitor().getPeriod());
    }

    @Bean
    @WebSocketScope
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance.heartbeat",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager loadBalanceConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties,
            WebSocketScopeHelper helper) {
        WebSocketLoadBalanceProperties.HeartbeatProperties heartbeat = properties.getLoadBalance().getHeartbeat();
        ScheduledExecutorServiceFactory factory = helper.getBean(ScheduledExecutorServiceFactory.class);
        return new ConnectionHeartbeatManager(
                Arrays.asList(Connection.Type.SUBSCRIBER, Connection.Type.OBSERVABLE),
                heartbeat.getTimeout(), heartbeat.getPeriod(),
                factory.create(ConnectionHeartbeatManager.class));
    }

    @Bean
    @WebSocketScope
    @ConditionalOnProperty(prefix = "concept.websocket.server.heartbeat",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager clientConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties,
            WebSocketScopeHelper helper) {
        WebSocketLoadBalanceProperties.HeartbeatProperties heartbeat = properties.getServer().getHeartbeat();
        ScheduledExecutorServiceFactory factory = helper.getBean(ScheduledExecutorServiceFactory.class);
        return new ConnectionHeartbeatManager(Connection.Type.CLIENT,
                heartbeat.getTimeout(), heartbeat.getPeriod(),
                factory.create(ConnectionHeartbeatManager.class));
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public WebSocketLoadBalanceConcept webSocketLoadBalanceConcept(WebSocketScopeHelper helper) {
        return new WebSocketLoadBalanceConcept.Builder()
                .connectionRepository(helper.getBean(ConnectionRepository.class))
                .connectionServerManager(helper.getBean(ConnectionServerManager.class))
                .connectionSubscriber(helper.getBean(ConnectionSubscriber.class))
                .addConnectionFactories(helper.getBeans(ConnectionFactory.class))
                .addConnectionSelectors(helper.getBeans(ConnectionSelector.class))
                .addMessageFactories(helper.getBeans(MessageFactory.class))
                .messageCodecAdapter(helper.getBean(MessageCodecAdapter.class))
                .eventPublisher(helper.getBean(ConnectionEventPublisher.class))
                .addEventListeners(helper.getBeans(ConnectionEventListener.class))
                .build();
    }
}
