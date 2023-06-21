package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.extension.ScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.monitor.LoadBalanceMonitorLogger;
import com.github.linyuzai.connection.loadbalance.core.monitor.ScheduledConnectionLoadBalanceMonitor;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

public class WebSocketLoadBalanceMonitorConfiguration {

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.logger", havingValue = "true", matchIfMissing = true)
    public ConnectionSubscribeLogger connectionSubscribeLogger() {
        Log log = LogFactory.getLog(ConnectionSubscribeLogger.class);
        return new ConnectionSubscribeLogger(log::info, log::error);
    }

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.monitor.logger", havingValue = "true", matchIfMissing = true)
    public LoadBalanceMonitorLogger loadBalanceMonitorLogger() {
        Log log = LogFactory.getLog(LoadBalanceMonitorLogger.class);
        return new LoadBalanceMonitorLogger(log::info, log::error);
    }

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.monitor.enabled", havingValue = "true", matchIfMissing = true)
    public ScheduledConnectionLoadBalanceMonitor scheduledConnectionLoadBalanceMonitor(
            WebSocketLoadBalanceProperties properties,
            ScheduledExecutorServiceFactory factory) {
        return new ScheduledConnectionLoadBalanceMonitor(
                factory.create(WebSocketScoped.NAME),
                properties.getLoadBalance().getMonitor().getPeriod());
    }

    @Bean
    @ConditionalOnProperty(value = "concept.websocket.load-balance.heartbeat.enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager loadBalanceConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties,
            ScheduledExecutorServiceFactory factory) {
        WebSocketLoadBalanceProperties.HeartbeatProperties heartbeat = properties.getLoadBalance().getHeartbeat();
        return new ConnectionHeartbeatManager(
                Arrays.asList(Connection.Type.SUBSCRIBER, Connection.Type.OBSERVABLE),
                heartbeat.getTimeout(), heartbeat.getPeriod(),
                factory.create(WebSocketScoped.NAME));
    }
}
