package com.github.linyuzai.connection.loadbalance.sse.reactive;


import com.github.linyuzai.connection.loadbalance.sse.SseDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.SseSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reactive SSE 负载均衡配置。
 * <p>
 * Reactive SSE load balancing configuration.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveSseLoadBalanceConfiguration {

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
    public static class WebSocketSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ReactiveSseConfiguration
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
                SseLoadBalanceConcept concept) {
            return new ReactiveSseServerEndpoint(idGenerator, factory, concept);
        }
    }
}
