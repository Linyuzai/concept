package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * Concept 初始化器。
 * 在服务启动后初始化。
 * <p>
 * Initializer of concept.
 * Init concept after server startup.
 *
 * @see ConnectionLoadBalanceConcept
 * @see ApplicationRunner
 */
@Getter
@RequiredArgsConstructor
public class ConnectionLoadBalanceConceptInitializer implements ApplicationRunner, Ordered {

    private final List<ConnectionLoadBalanceConcept> concepts;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        concepts.forEach(ConnectionLoadBalanceConcept::initialize);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
