package com.bytedance.juejin.rpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.event.EventListener;

import java.util.*;

/**
 * 负载均衡服务路由
 */
@Slf4j
public class RouterLoadBalancerClientFactory extends LoadBalancerClientFactory {

    private final DiscoveryClient discoveryClient;

    private volatile Map<String, String> routerMap = Collections.emptyMap();

    public RouterLoadBalancerClientFactory(LoadBalancerClientsProperties properties, DiscoveryClient discoveryClient) {
        super(properties);
        this.discoveryClient = discoveryClient;
    }

    @Override
    public <T> T getInstance(String name, Class<T> type) {
        String router = getRouter(name);
        log.info("Router mapping: {} => {}", name, router);
        return super.getInstance(router, type);
    }

    protected String getRouter(String name) {
        return routerMap.getOrDefault(name, name);
    }

    /**
     * 监听心跳事件
     */
    @EventListener
    public void refreshRouters(HeartbeatEvent event) {
        //新的路由映射
        Map<String, String> newRouterMap = new HashMap<>();
        //获得服务名
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            //获得服务实例
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            if (instances.isEmpty()) {
                continue;
            }
            //这里直接拿第一个
            ServiceInstance instance = instances.get(0);
            //获得 metadata 中的 routers
            String routersMetadata = instance.getMetadata()
                    .getOrDefault("routers", "");
            String[] routers = routersMetadata.split(",");

            for (String router : routers) {
                if (router.isEmpty()) {
                    continue;
                }
                newRouterMap.put(router, service);
            }
        }
        if (!this.routerMap.equals(newRouterMap)) {
            log.info("Update routers mapping => {}", newRouterMap);
        }
        //更新缓存
        this.routerMap = newRouterMap;
    }
}
