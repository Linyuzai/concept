package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ApplicationConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.monitor.ConnectionSubscribeMonitor;
import com.github.linyuzai.connection.loadbalance.core.subscribe.monitor.ScheduledExecutorConnectionSubscribeMonitor;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionEventPublisher connectionEventPublisher(ApplicationEventPublisher publisher) {
        return new ApplicationConnectionEventPublisher(publisher);
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public WebSocketLoadBalanceConcept webSocketLoadBalanceConcept(
            ConnectionServerProvider provider,
            ConnectionSubscriber subscriber,
            List<ConnectionFactory> connectionFactories,
            List<ConnectionSelector> connectionSelectors,
            MessageCodecAdapter messageCodecAdapter,
            List<MessageFactory> messageFactories,
            ConnectionEventPublisher eventPublisher,
            List<ConnectionEventListener> eventListeners) {
        return new WebSocketLoadBalanceConcept.Builder()
                .connectionServerProvider(provider)
                .connectionSubscriber(subscriber)
                .addConnectionFactories(connectionFactories)
                .addConnectionSelectors(connectionSelectors)
                .messageCodecAdapter(messageCodecAdapter)
                .addMessageFactories(messageFactories)
                .eventPublisher(eventPublisher)
                .addEventListeners(eventListeners)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "concept.websocket.load-balance.subscriber.auto-subscribe.enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionSubscribeMonitor connectionAutoSubscriber(WebSocketLoadBalanceConcept concept,
                                                               WebSocketLoadBalanceProperties properties) {
        return new ScheduledExecutorConnectionSubscribeMonitor(concept,
                properties.getSubscriber().getMonitor().getPeriod());
    }
}
