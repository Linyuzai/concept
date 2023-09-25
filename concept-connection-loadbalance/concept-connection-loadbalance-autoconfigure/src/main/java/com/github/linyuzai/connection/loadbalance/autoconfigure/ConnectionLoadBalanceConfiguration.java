package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.autoconfigure.discovery.DiscoveryConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.event.ApplicationConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.logger.LoggedErrorHandler;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.TypeGroupedConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.server.SimpleConnectionServerManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * 连接负载均衡配置
 * <p>
 * Configuration of connection's load-balance
 */
@Configuration(proxyBeanMethods = false)
public class ConnectionLoadBalanceConfiguration {

    @ConditionalOnClass({DiscoveryClient.class, Registration.class})
    @Configuration(proxyBeanMethods = false)
    public static class DiscoveryConnectionServerManagerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ConnectionServerManagerFactory connectionServerManagerFactory(DiscoveryClient client,
                                                                             Registration registration) {
            DiscoveryConnectionServerManagerFactory factory = new DiscoveryConnectionServerManagerFactory();
            factory.setDiscoveryClient(client);
            factory.setRegistration(registration);
            return factory;
        }
    }

    @ConditionalOnMissingClass({
            "org.springframework.cloud.client.discovery.DiscoveryClient",
            "org.springframework.cloud.client.serviceregistry.Registration"})
    @Configuration(proxyBeanMethods = false)
    public static class ImplConnectionServerManagerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ConnectionServerManagerFactory connectionServerManagerFactory() {
            return new SimpleConnectionServerManagerFactory();
        }
    }

    @Bean
    public ConnectionRepositoryFactory connectionRepositoryFactory() {
        return new TypeGroupedConnectionRepositoryFactory();
    }

    @Bean
    @Order(0)
    public ErrorHandler errorHandler() {
        return new LoggedErrorHandler();
    }

    @Bean
    public ConnectionEventPublisherFactory connectionEventPublisherFactory(ApplicationEventPublisher publisher) {
        return new ApplicationConnectionEventPublisherFactory(publisher);
    }

    @Bean
    public ConnectionLoadBalanceConceptInitializer connectionLoadBalanceConceptInitializer(
            List<ConnectionLoadBalanceConcept> concepts) {
        return new ConnectionLoadBalanceConceptInitializer(concepts);
    }
}
