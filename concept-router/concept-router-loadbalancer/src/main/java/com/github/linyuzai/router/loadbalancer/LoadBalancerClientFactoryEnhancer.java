package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.RouterConcept;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class LoadBalancerClientFactoryEnhancer implements BeanPostProcessor {

    private final RouterConcept concept;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof LoadBalancerClientFactory) {
            return new RouterLoadBalancerClientFactory((LoadBalancerClientFactory) bean, concept);
        }
        return bean;
    }
}
