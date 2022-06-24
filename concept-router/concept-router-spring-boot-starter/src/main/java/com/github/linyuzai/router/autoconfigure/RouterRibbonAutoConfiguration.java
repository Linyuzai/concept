package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.annotation.ConditionalOnRouterEnabled;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.ribbon.feign.CachingSpringLoadBalancerFactoryEnhancer;
import com.github.linyuzai.router.ribbon.gateway.v1.LoadBalancerClientFilterV1Enhancer;
import com.github.linyuzai.router.ribbon.gateway.v2.LoadBalancerClientFilterV2Enhancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnRouterEnabled
@ConditionalOnClass(LoadBalancerClientFilter.class)
@Configuration(proxyBeanMethods = false)
public class RouterRibbonAutoConfiguration {

    @ConditionalOnRouterEnabled
    @ConditionalOnClass(LoadBalancerClientFilter.class)
    @ConditionalOnMissingClass("org.springframework.cloud.gateway.config.LoadBalancerProperties")
    @Configuration(proxyBeanMethods = false)
    public static class GatewayV1Configuration {

        @Bean
        public LoadBalancerClientFilterV1Enhancer loadBalancerClientFilterV1Enhancer(ApplicationContext context,
                                                                                     RouterConcept concept) {
            return new LoadBalancerClientFilterV1Enhancer(context, concept);
        }
    }

    @ConditionalOnRouterEnabled
    @ConditionalOnClass({LoadBalancerClientFilter.class, LoadBalancerProperties.class})
    @Configuration(proxyBeanMethods = false)
    public static class GatewayV2Configuration {

        @Bean
        public LoadBalancerClientFilterV2Enhancer loadBalancerClientFilterV2Enhancer(ApplicationContext context,
                                                                                     RouterConcept concept) {
            return new LoadBalancerClientFilterV2Enhancer(context, concept);
        }
    }

    @ConditionalOnRouterEnabled
    @ConditionalOnClass(CachingSpringLoadBalancerFactory.class)
    @Configuration(proxyBeanMethods = false)
    public static class FeignConfiguration {

        @Bean
        public CachingSpringLoadBalancerFactoryEnhancer cachingSpringLoadBalancerFactoryEnhancer(RouterConcept concept) {
            return new CachingSpringLoadBalancerFactoryEnhancer(concept);
        }
    }
}
