package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.management.DefaultRouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterManagementController;
import com.github.linyuzai.router.core.concept.DefaultRouterConcept;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.core.locator.RouterLocator;
import com.github.linyuzai.router.core.matcher.RouterMatcher;
import com.github.linyuzai.router.core.repository.JacksonLocalRouterRepository;
import com.github.linyuzai.router.core.repository.RouterRepository;
import com.github.linyuzai.router.loadbalancer.LoadBalancerClientFactoryEnhancer;
import com.github.linyuzai.router.loadbalancer.LoadbalancerRequestMatcher;
import com.github.linyuzai.router.loadbalancer.LoadbalancerServiceLocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterAutoConfiguration {

    @SuppressWarnings("all")
    @Bean(initMethod = "initialize")
    @ConditionalOnMissingBean
    public RouterRepository routerRepository() {
        return new JacksonLocalRouterRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterMatcher routerMatcher() {
        return new LoadbalancerRequestMatcher();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterLocator routerLocator() {
        return new LoadbalancerServiceLocator();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterConcept routerConcept(RouterRepository repository,
                                       RouterMatcher matcher,
                                       RouterLocator locator) {
        return new DefaultRouterConcept(repository, matcher, locator);
    }

    @Bean
    public LoadBalancerClientFactoryEnhancer loadBalancerClientFactoryEnhancer(RouterConcept concept) {
        return new LoadBalancerClientFactoryEnhancer(concept);
    }

    @Bean
    public RouterConvertor routerConvertor() {
        return new DefaultRouterConvertor();
    }

    @Bean
    public RouterManagementController routerManagementController() {
        return new RouterManagementController();
    }
}
