package $PACKAGE$.basic.rpc;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.event.EventListener;

import java.util.*;

/**
 * 该类重写负载均衡的路由规则
 * <p>
 * 用于 Feign 这类 RPC 组件的服务路由
 * <p>
 * https://juejin.cn/post/7202807471882731580
 */
public class $CLASS$LoadBalancerClientFactory extends LoadBalancerClientFactory {

    private final DiscoveryClient discoveryClient;

    private volatile Map<String, String> routerMap = Collections.emptyMap();

    public $CLASS$LoadBalancerClientFactory(LoadBalancerClientsProperties properties, DiscoveryClient discoveryClient) {
        super(properties);
        this.discoveryClient = discoveryClient;
    }

    @Override
    public <T> T getInstance(String name, Class<T> type) {
        String router = getRouter(name);
        //log.info("Router mapping: {} => {}", name, router);
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
                newRouterMap.put(router, service);
            }
        }
        if (!this.routerMap.equals(newRouterMap)) {
            //log.info("Update router map => {}", newRouterMap);
        }
        //更新缓存
        this.routerMap = newRouterMap;
    }
}
