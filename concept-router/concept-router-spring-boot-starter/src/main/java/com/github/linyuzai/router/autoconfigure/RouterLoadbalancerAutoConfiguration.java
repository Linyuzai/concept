package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.core.locator.RouterLocator;
import com.github.linyuzai.router.core.matcher.RouterMatcher;
import com.github.linyuzai.router.loadbalancer.LoadBalancerClientFactoryEnhancer;
import com.github.linyuzai.router.loadbalancer.LoadbalancerRequestRouterMatcher;
import com.github.linyuzai.router.loadbalancer.LoadbalancerServiceRouterLocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = {"concept.router.enabled", "spring.cloud.loadbalancer.enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class RouterLoadbalancerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RouterMatcher routerMatcher() {
        return new LoadbalancerRequestRouterMatcher();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterLocator routerLocator() {
        return new LoadbalancerServiceRouterLocator();
    }

    @ConditionalOnProperty(name = {"concept.router.enabled", "spring.cloud.loadbalancer.enabled"}, havingValue = "true", matchIfMissing = true)
    @Configuration(proxyBeanMethods = false)
    public static class RouterEnhancerAutoConfiguration {

        @Bean
        public LoadBalancerClientFactoryEnhancer loadBalancerClientFactoryEnhancer(RouterConcept concept) {
            return new LoadBalancerClientFactoryEnhancer(concept);
        }
    }
}
