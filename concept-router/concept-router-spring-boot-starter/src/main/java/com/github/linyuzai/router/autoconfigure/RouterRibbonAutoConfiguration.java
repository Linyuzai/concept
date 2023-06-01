package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.annotation.ConditionalOnRouterEnabled;
import com.github.linyuzai.router.ribbon.feign.RibbonFeignEnhancer;
import com.github.linyuzai.router.ribbon.gateway.v1.RibbonGatewayV1Enhancer;
import com.github.linyuzai.router.ribbon.gateway.v2.RibbonGatewayV2Enhancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnRouterEnabled
@Configuration(proxyBeanMethods = false)
public class RouterRibbonAutoConfiguration {

    @ConditionalOnRouterEnabled
    @ConditionalOnClass(name = "org.springframework.cloud.gateway.filter.LoadBalancerClientFilter")
    @ConditionalOnMissingClass("org.springframework.cloud.gateway.config.LoadBalancerProperties")
    @Configuration(proxyBeanMethods = false)
    public static class GatewayV1Configuration {

        @Bean
        public static RibbonGatewayV1Enhancer ribbonGatewayV1Enhancer() {
            return new RibbonGatewayV1Enhancer();
        }
    }

    @ConditionalOnRouterEnabled
    @ConditionalOnClass(name = {
            "org.springframework.cloud.gateway.filter.LoadBalancerClientFilter",
            "org.springframework.cloud.gateway.config.LoadBalancerProperties"})
    @Configuration(proxyBeanMethods = false)
    public static class GatewayV2Configuration {

        @Bean
        public static RibbonGatewayV2Enhancer ribbonGatewayV2Enhancer() {
            return new RibbonGatewayV2Enhancer();
        }
    }

    @ConditionalOnRouterEnabled
    @ConditionalOnClass(name = "org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory")
    @Configuration(proxyBeanMethods = false)
    public static class FeignConfiguration {

        @Bean
        public static RibbonFeignEnhancer ribbonFeignEnhancer() {
            return new RibbonFeignEnhancer();
        }
    }
}
