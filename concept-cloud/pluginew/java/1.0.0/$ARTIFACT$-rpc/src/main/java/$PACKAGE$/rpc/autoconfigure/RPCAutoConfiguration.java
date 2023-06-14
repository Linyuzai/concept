package $PACKAGE$.rpc.autoconfigure;

import $PACKAGE$.rpc.RouterLoadBalancerClientFactory;
import $PACKAGE$.rpc.sample.RPCSampleFacadeAdapter;
import $PACKAGE$.rpc.sample.RPCSampleFacadeAdapterImpl;
import $PACKAGE$.rpc.user.RPCUserFacadeAdapter;
import $PACKAGE$.rpc.user.RPCUserFacadeAdapterImpl;
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
    public RPCSampleFacadeAdapter rpcSampleFacadeAdapter() {
        return new RPCSampleFacadeAdapterImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RPCUserFacadeAdapter rpcUserFacadeAdapter() {
        return new RPCUserFacadeAdapterImpl();
    }
}
