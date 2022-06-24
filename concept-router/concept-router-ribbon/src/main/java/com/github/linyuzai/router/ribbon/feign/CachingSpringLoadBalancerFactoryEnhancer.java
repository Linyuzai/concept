package com.github.linyuzai.router.ribbon.feign;

import com.github.linyuzai.router.core.concept.RouterConcept;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class CachingSpringLoadBalancerFactoryEnhancer implements BeanPostProcessor {

    private final RouterConcept concept;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof CachingSpringLoadBalancerFactory) {
            return new RouterCachingSpringLoadBalancerFactory((CachingSpringLoadBalancerFactory) bean, concept);
        }
        return bean;
    }
}
