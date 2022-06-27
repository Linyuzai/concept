package com.github.linyuzai.router.ribbon.gateway.v1;

import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.ribbon.RouterSpringClientFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class RibbonGatewayV1Enhancer implements BeanPostProcessor {

    private final ApplicationContext context;

    private final RouterConcept concept;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof LoadBalancerClientFilter) {
            if (bean instanceof RouterLoadBalancerClientFilterV1) {
                return bean;
            }
            return new RouterLoadBalancerClientFilterV1(context);
        }
        if (bean instanceof SpringClientFactory) {
            if (bean instanceof RouterSpringClientFactory) {
                return bean;
            }
            return new RouterSpringClientFactory((SpringClientFactory) bean, concept);
        }
        return bean;
    }
}
