package com.github.linyuzai.router.ribbon;

import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.core.concept.UriRouterSource;
import com.netflix.client.IClient;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RouterSpringClientFactory extends SpringClientFactory {

    private final SpringClientFactory factory;

    private final RouterConcept concept;

    @Override
    public <C extends IClient<?, ?>> C getClient(String name, Class<C> clientClass) {
        return factory.getClient(name, clientClass);
    }

    @Override
    public ILoadBalancer getLoadBalancer(String name) {
        return new RouterILoadBalancer(name, factory.getLoadBalancer(name), concept);
    }

    @Override
    public IClientConfig getClientConfig(String name) {
        return factory.getClientConfig(name);
    }

    @Override
    public RibbonLoadBalancerContext getLoadBalancerContext(String serviceId) {
        return factory.getLoadBalancerContext(serviceId);
    }

    @Override
    public <C> C getInstance(String name, Class<C> type) {
        return factory.getInstance(name, type);
    }

    @Override
    public void setApplicationContext(ApplicationContext parent) throws BeansException {
        factory.setApplicationContext(parent);
    }

    @Override
    public void setConfigurations(List<RibbonClientSpecification> configurations) {
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

    @Override
    public <T> Map<String, T> getInstances(String name, Class<T> type) {
        return factory.getInstances(name, type);
    }

    @AllArgsConstructor
    public static class RouterILoadBalancer implements ILoadBalancer {

        private final String serviceId;

        private final ILoadBalancer loadBalancer;

        private final RouterConcept concept;

        @Override
        public void addServers(List<Server> newServers) {
            loadBalancer.addServers(newServers);
        }

        @Override
        public Server chooseServer(Object key) {
            if (key instanceof URI) {
                URI uri = (URI) key;
                List<Server> servers = getReachableServers();
                List<RibbonServerRouterLocation> locations = servers.stream()
                        .map(it -> new RibbonServerRouterLocation(serviceId, it))
                        .collect(Collectors.toList());
                Router.Source source = new UriRouterSource(serviceId, uri);
                Router.Location location = concept.route(source, locations);
                if (location == Router.Location.UNMATCHED) {
                    return loadBalancer.chooseServer(key);
                } else if (location == Router.Location.UNAVAILABLE) {
                    return null;
                } else {
                    RibbonServerRouterLocation l = (RibbonServerRouterLocation) location;
                    return l.getServer();
                }
            }
            return loadBalancer.chooseServer(key);
        }

        @Override
        public void markServerDown(Server server) {
            loadBalancer.markServerDown(server);
        }

        @Override
        public List<Server> getServerList(boolean availableOnly) {
            return loadBalancer.getServerList(availableOnly);
        }

        @Override
        public List<Server> getReachableServers() {
            return loadBalancer.getReachableServers();
        }

        @Override
        public List<Server> getAllServers() {
            return loadBalancer.getAllServers();
        }
    }
}
