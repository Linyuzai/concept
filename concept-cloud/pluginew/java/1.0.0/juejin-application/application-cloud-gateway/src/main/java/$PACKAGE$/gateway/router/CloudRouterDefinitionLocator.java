package $PACKAGE$.gateway.router;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 向注册中心注册自身服务支持的模块
 */
@Component
@RequiredArgsConstructor
public class CloudRouterDefinitionLocator implements RouteDefinitionLocator {

    /**
     * 服务发现组件
     */
    private final DiscoveryClient discoveryClient;

    /**
     * 路由缓存
     */
    private volatile List<RouteDefinition> routeDefinitions = Collections.emptyList();

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routeDefinitions);
    }

    /**
     * 监听心跳事件
     */
    @EventListener
    public void refreshRouters(HeartbeatEvent event) {
        //新的路由
        List<RouteDefinition> newRouteDefinitions = new ArrayList<>();
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
            //生成新的 RouteDefinition
            for (String router : routers) {
                if (router.isEmpty()) {
                    continue;
                }
                RouteDefinition rd = new RouteDefinition();
                rd.getMetadata().put("router", router);
                rd.setId("router@" + service);
                rd.setUri(URI.create("lb://" + service));
                PredicateDefinition pd = new PredicateDefinition();
                pd.setName("Path");
                pd.addArg("arg0", "/" + router + "/**");
                rd.setPredicates(Collections.singletonList(pd));
                FilterDefinition fd = new FilterDefinition();
                fd.setName("StripPrefix");
                fd.addArg("arg0", "1");
                rd.setFilters(Collections.singletonList(fd));
                newRouteDefinitions.add(rd);
            }
        }
        //更新缓存
        this.routeDefinitions = newRouteDefinitions;
    }
}
