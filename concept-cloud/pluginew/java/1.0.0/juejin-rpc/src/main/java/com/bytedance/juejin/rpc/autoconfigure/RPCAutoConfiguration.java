package com.bytedance.juejin.rpc.autoconfigure;

import com.bytedance.juejin.rpc.RouterLoadBalancerClientFactory;
import com.bytedance.juejin.rpc.user.RPCUserFacadeAdapter;
import com.bytedance.juejin.rpc.user.RPCUserFacadeAdapterImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RPCAutoConfiguration {

    @Bean
    public LoadBalancerClientFactory routerLoadBalancerClientFactory(LoadBalancerClientsProperties properties,
                                                                     DiscoveryClient discoveryClient) {
        return new RouterLoadBalancerClientFactory(properties, discoveryClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public RPCUserFacadeAdapter rpcUserFacadeAdapter() {
        return new RPCUserFacadeAdapterImpl();
    }
}
