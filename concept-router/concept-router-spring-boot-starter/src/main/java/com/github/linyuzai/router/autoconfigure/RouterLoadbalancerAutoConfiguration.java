package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.annotation.ConditionalOnRouterEnabled;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.loadbalancer.LoadBalancerClientFactoryEnhancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnRouterEnabled
@Configuration(proxyBeanMethods = false)
public class RouterLoadbalancerAutoConfiguration {

    @Bean
    @ConditionalOnClass(LoadBalancerClientFactory.class)
    public LoadBalancerClientFactoryEnhancer loadBalancerClientFactoryEnhancer(RouterConcept concept) {
        return new LoadBalancerClientFactoryEnhancer(concept);
    }
}
