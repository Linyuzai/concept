package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.RouterConcept;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class RouterLoadBalancerClientFactory extends LoadBalancerClientFactory {

    private final LoadBalancerClientFactory factory;

    private final RouterConcept concept;

    @Override
    public ReactiveLoadBalancer<ServiceInstance> getInstance(String serviceId) {
        ReactiveLoadBalancer<ServiceInstance> instance = factory.getInstance(serviceId);
        if (instance instanceof RouterReactorLoadbalancer) {
            return instance;
        }
        return new RouterReactorLoadbalancer(serviceId, factory, instance, concept);
    }

    @Override
    public LoadBalancerProperties getProperties(String serviceId) {
        return factory.getProperties(serviceId);
    }

    @Override
    public void setApplicationContext(ApplicationContext parent) throws BeansException {
        factory.setApplicationContext(parent);
    }

    @Override
    public ApplicationContext getParent() {
        return factory.getParent();
    }

    @Override
    public void setConfigurations(List<LoadBalancerClientSpecification> configurations) {
        factory.setConfigurations(configurations);
    }

    @Override
    public Set<String> getContextNames() {
        return factory.getContextNames();
    }

    @Override
    public void destroy() {
        factory.destroy();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInstance(String name, Class<T> type) {
        T instance = factory.getInstance(name, type);
        if (ReactorServiceInstanceLoadBalancer.class.isAssignableFrom(type)) {
            if (instance instanceof RouterReactorLoadbalancer) {
                return instance;
            }
            return (T) new RouterReactorLoadbalancer(name, factory, (ReactiveLoadBalancer<ServiceInstance>) instance, concept);
        }
        return instance;
    }

    @Override
    public <T> ObjectProvider<T> getLazyProvider(String name, Class<T> type) {
        return factory.getLazyProvider(name, type);
    }

    @Override
    public <T> ObjectProvider<T> getProvider(String name, Class<T> type) {
        return factory.getProvider(name, type);
    }

    @Override
    public <T> T getInstance(String name, Class<?> clazz, Class<?>... generics) {
        return factory.getInstance(name, clazz, generics);
    }

    @Override
    public <T> T getInstance(String name, ResolvableType type) {
        return factory.getInstance(name, type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getInstances(String name, Class<T> type) {
        Map<String, T> instances = factory.getInstances(name, type);
        if (ReactorServiceInstanceLoadBalancer.class.isAssignableFrom(type)) {
            for (Map.Entry<String, T> entry : instances.entrySet()) {
                T value = entry.getValue();
                if (value instanceof RouterReactorLoadbalancer) {
                    continue;
                }
                entry.setValue((T) new RouterReactorLoadbalancer(name, factory,
                        (ReactiveLoadBalancer<ServiceInstance>) value, concept));
            }
        }
        return instances;
    }
}
