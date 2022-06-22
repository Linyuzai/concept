package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.annotation.ConditionalOnRouterEnabled;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.ribbon.LoadBalancerClientFilterEnhancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnRouterEnabled
@Configuration(proxyBeanMethods = false)
public class RouterRibbonAutoConfiguration {

    @ConditionalOnClass(LoadBalancerClientFilter.class)
    @Bean
    public LoadBalancerClientFilterEnhancer loadBalancerClientFilterEnhancer(ApplicationContext context,
                                                                             RouterConcept concept) {
        return new LoadBalancerClientFilterEnhancer(context, concept);
    }
}
