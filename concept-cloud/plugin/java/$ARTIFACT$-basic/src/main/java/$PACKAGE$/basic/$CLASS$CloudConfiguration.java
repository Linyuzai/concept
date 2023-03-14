package $PACKAGE$.basic;

import $PACKAGE$.basic.rpc.$CLASS$LoadBalancerClientFactory;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这里添加微服务需要而单体应用不需要注入的组件
 */
@Configuration
public class $CLASS$CloudConfiguration {

    @Bean
    public LoadBalancerClientFactory loadBalancerClientFactory(LoadBalancerClientsProperties properties,
                                                                     DiscoveryClient discoveryClient) {
        return new $CLASS$LoadBalancerClientFactory(properties, discoveryClient);
    }
}
