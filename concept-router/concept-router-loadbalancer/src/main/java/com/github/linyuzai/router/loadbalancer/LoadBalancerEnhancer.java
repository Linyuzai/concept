package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.RouterConcept;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * 增强 {@link LoadBalancerClientFactory}
 */
public class LoadBalancerEnhancer implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof LoadBalancerClientFactory) {
            if (bean instanceof RouterLoadBalancerClientFactory) {
                return bean;
            }
            RouterConcept concept = applicationContext.getBean(RouterConcept.class);
            return new RouterLoadBalancerClientFactory((LoadBalancerClientFactory) bean, concept);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
