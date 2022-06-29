package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.RouterConcept;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.lang.NonNull;

/**
 * 增强 {@link LoadBalancerClientFactory}
 */
@AllArgsConstructor
public class LoadBalancerEnhancer implements BeanPostProcessor {

    private final RouterConcept concept;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof LoadBalancerClientFactory) {
            if (bean instanceof RouterLoadBalancerClientFactory) {
                return bean;
            }
            return new RouterLoadBalancerClientFactory((LoadBalancerClientFactory) bean, concept);
        }
        return bean;
    }
}
