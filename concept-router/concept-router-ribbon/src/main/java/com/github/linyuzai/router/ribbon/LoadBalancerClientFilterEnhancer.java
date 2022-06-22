package com.github.linyuzai.router.ribbon;

import com.github.linyuzai.router.core.concept.RouterConcept;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class LoadBalancerClientFilterEnhancer implements BeanPostProcessor {

    private final ApplicationContext context;

    private final RouterConcept concept;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof LoadBalancerClientFilter) {
            return new RouterLoadBalancerClientFilter(context, concept);
        }
        return bean;
    }
}
