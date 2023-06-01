package com.github.linyuzai.router.ribbon.feign;

import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.ribbon.RouterSpringClientFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * 增强 {@link CachingSpringLoadBalancerFactory} 和 {@link SpringClientFactory}
 */
public class RibbonFeignEnhancer implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof CachingSpringLoadBalancerFactory) {
            if (bean instanceof RouterCachingSpringLoadBalancerFactory) {
                return bean;
            }
            return new RouterCachingSpringLoadBalancerFactory((CachingSpringLoadBalancerFactory) bean);
        }
        if (bean instanceof SpringClientFactory) {
            if (bean instanceof RouterSpringClientFactory) {
                return bean;
            }
            RouterConcept concept = applicationContext.getBean(RouterConcept.class);
            return new RouterSpringClientFactory((SpringClientFactory) bean, concept);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
