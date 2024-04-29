package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.net.URI;

public interface ServletSseLoadBalanceRunner {

    void run(Runnable runnable, URI uri, ConnectionLoadBalanceConcept concept);
}
