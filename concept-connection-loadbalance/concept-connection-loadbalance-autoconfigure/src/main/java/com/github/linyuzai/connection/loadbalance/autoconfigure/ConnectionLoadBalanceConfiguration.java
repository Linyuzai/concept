package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.autoconfigure.discovery.DiscoveryConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.event.ApplicationConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.logger.ErrorLogger;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactoryImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 连接负载均衡配置
 */
@Configuration(proxyBeanMethods = false)
public class ConnectionLoadBalanceConfiguration {

    @ConditionalOnClass({DiscoveryClient.class, Registration.class})
    @Configuration(proxyBeanMethods = false)
    public static class DiscoveryConnectionServerManagerConfiguration {

        @Bean
        public ConnectionServerManagerFactory connectionServerManagerFactory(DiscoveryClient client,
                                                                                      Registration registration) {
            return new DiscoveryConnectionServerManagerFactory(client, registration);
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
            return new ConnectionServerManagerFactoryImpl();
        }
    }

    @Bean
    public ConnectionRepositoryFactory connectionRepositoryFactory() {
        return new ConnectionRepositoryFactoryImpl();
    }

    @Bean
    public ErrorHandler errorHandler() {
        Log log = LogFactory.getLog(ErrorLogger.class);
        return new ErrorLogger(log::info, log::error);
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
