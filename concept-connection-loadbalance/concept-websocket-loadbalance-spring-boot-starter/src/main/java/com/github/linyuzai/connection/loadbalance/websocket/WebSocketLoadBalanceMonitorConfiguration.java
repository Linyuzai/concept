package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.extension.ScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.monitor.LoadBalanceMonitorLogger;
import com.github.linyuzai.connection.loadbalance.core.monitor.ScheduledConnectionLoadBalanceMonitor;
import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeHandler;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class WebSocketLoadBalanceMonitorConfiguration {

    @Bean
    public ConnectionSubscribeHandler connectionSubscribeHandler() {
        return new ConnectionSubscribeHandler().addScopes(WebSocketScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.logger", havingValue = "true", matchIfMissing = true)
    public ConnectionSubscribeLogger connectionSubscribeLogger() {
        Log log = LogFactory.getLog(ConnectionSubscribeLogger.class);
        return new ConnectionSubscribeLogger(log::info, log::error)
                .addScopes(WebSocketScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.monitor.logger", havingValue = "true", matchIfMissing = true)
    public LoadBalanceMonitorLogger loadBalanceMonitorLogger() {
        Log log = LogFactory.getLog(LoadBalanceMonitorLogger.class);
        return new LoadBalanceMonitorLogger(log::info, log::error)
                .addScopes(WebSocketScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.monitor.enabled", havingValue = "true", matchIfMissing = true)
    public ScheduledConnectionLoadBalanceMonitor scheduledConnectionLoadBalanceMonitor(
            WebSocketLoadBalanceProperties properties,
            List<ScheduledExecutorServiceFactory> factories) {
        long period = properties.getLoadBalance().getMonitor().getPeriod();
        ScheduledExecutorService service = ScopedFactory
                .create(WebSocketScoped.NAME, ScheduledExecutorService.class, factories);
        return new ScheduledConnectionLoadBalanceMonitor(service, period)
                .addScopes(WebSocketScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.heartbeat.enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager loadBalanceConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties,
            List<ScheduledExecutorServiceFactory> factories) {
        long timeout = properties.getLoadBalance().getHeartbeat().getTimeout();
        long period = properties.getLoadBalance().getHeartbeat().getPeriod();
        ScheduledExecutorService service = ScopedFactory
                .create(WebSocketScoped.NAME, ScheduledExecutorService.class, factories);
        return new ConnectionHeartbeatManager(
                Arrays.asList(Connection.Type.SUBSCRIBER, Connection.Type.OBSERVABLE),
                timeout, period, service).addScopes(WebSocketScoped.NAME);
    }
}
