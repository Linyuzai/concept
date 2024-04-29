package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.sse.SseDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.SseSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet SSE 负载均衡配置。
 * <p>
 * Servlet SSE load balancing configuration.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletSseLoadBalanceConfiguration {

    @Bean
    public ServletSseConnectionFactory servletSseConnectionFactory() {
        return new ServletSseConnectionFactory();
    }

    @Bean
    public ServletSseMessageCodecAdapter servletSseMessageCodecAdapter() {
        return new ServletSseMessageCodecAdapter();
    }

    /*@Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(EventSource.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "SSE", matchIfMissing = true)
    public static class OkHttpSseSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.OkHttpSseConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }*/

    /*@Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(EventSource.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "SSE_SSL")
    public static class OkHttpSseSSLSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.OkHttpSseSSLConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }*/

    @Configuration(proxyBeanMethods = false)
    //@ConditionalOnMissingClass("okhttp3.sse.EventSource")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "SSE", matchIfMissing = true)
    public static class SseSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ServletSseConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    //@ConditionalOnMissingClass("okhttp3.sse.EventSource")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "SSE_SSL")
    public static class SseSSLSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ServletSseSSLConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.sse.server.default-endpoint.enabled",
            havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends SseDefaultEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SseEmitterFactory sseEmitterFactory() {
            return new DefaultSseEmitterFactory();
        }

        @Bean
        @ConditionalOnMissingBean
        public ServletSseServerEndpoint servletSseServerEndpoint(
                SseIdGenerator idGenerator,
                SseEmitterFactory factory,
                SseLoadBalanceConcept concept) {
            return new ServletSseServerEndpoint(idGenerator, factory, concept);
        }
    }
}
