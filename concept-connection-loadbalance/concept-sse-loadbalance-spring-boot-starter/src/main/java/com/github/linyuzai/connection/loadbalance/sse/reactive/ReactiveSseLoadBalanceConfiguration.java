package com.github.linyuzai.connection.loadbalance.sse.reactive;


import com.github.linyuzai.connection.loadbalance.sse.SseDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.SseLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.SseSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Reactive SSE 负载均衡配置。
 * <p>
 * Reactive SSE load balancing configuration.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveSseLoadBalanceConfiguration extends SseLoadBalanceConfiguration {

    @Bean
    public ReactiveSseConnectionFactory reactiveSseConnectionFactory() {
        return new ReactiveSseConnectionFactory();
    }

    @Bean
    public ReactiveSseMessageCodecAdapter reactiveSseMessageCodecAdapter() {
        return new ReactiveSseMessageCodecAdapter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "SSE", matchIfMissing = true)
    public static class SseSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ReactiveSseConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "SSE_SSL")
    public static class SseSSLSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ReactiveSseSSLConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.sse.server.default-endpoint.enabled",
            havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends SseDefaultEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SseFluxFactory sseFluxFactory() {
            return new DefaultSseFluxFactory();
        }

        @Bean
        @ConditionalOnMissingBean
        public ReactiveSseServerEndpoint reactiveSseServerEndpoint(
                SseIdGenerator idGenerator,
                SseFluxFactory factory,
                SseLoadBalanceConcept concept,
                List<SseRequestInterceptor> interceptors) {
            return new ReactiveSseServerEndpoint(idGenerator, factory, concept, interceptors);
        }
    }
}
