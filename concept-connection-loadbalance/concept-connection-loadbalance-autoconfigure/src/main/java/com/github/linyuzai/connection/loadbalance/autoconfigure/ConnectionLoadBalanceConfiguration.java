package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.autoconfigure.event.ApplicationConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.logger.ErrorLogger;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactoryImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 连接负载均衡配置
 */
@Configuration(proxyBeanMethods = false)
public class ConnectionLoadBalanceConfiguration {

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
    public ConnectionEventPublisher connectionEventPublisher(ApplicationEventPublisher publisher) {
        return new ApplicationConnectionEventPublisher(publisher);
    }
}
