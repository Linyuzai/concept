package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * {@link ConnectionLoadBalanceConcept} 初始化器
 */
@AllArgsConstructor
public class ConnectionLoadBalanceConceptInitializer implements ApplicationRunner, Ordered {

    private List<ConnectionLoadBalanceConcept> concepts;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        concepts.forEach(ConnectionLoadBalanceConcept::initialize);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
