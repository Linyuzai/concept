package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;

@AllArgsConstructor
public class ConnectionLoadBalanceConceptInitializer implements ApplicationRunner {

    private List<ConnectionLoadBalanceConcept> concepts;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        concepts.forEach(ConnectionLoadBalanceConcept::initialize);
    }
}
