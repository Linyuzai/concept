package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.discovery.DiscoveryConnectionServerProvider;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@CommonsLog
@Configuration(proxyBeanMethods = false)
public class ConnectionLoadBalanceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({DiscoveryClient.class, Registration.class})
    public ConnectionServerProvider connectionServerProvider(DiscoveryClient client, Registration registration) {
        return new DiscoveryConnectionServerProvider(client, registration);
    }

    @Bean
    public ConnectionSubscribeLogger connectionSubscribeLogger() {
        return new ConnectionSubscribeLogger(log::info, log::error);
    }
}
