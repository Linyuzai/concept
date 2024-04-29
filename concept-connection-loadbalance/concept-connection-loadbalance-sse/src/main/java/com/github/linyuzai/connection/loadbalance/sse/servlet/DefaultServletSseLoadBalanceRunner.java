package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultServletSseLoadBalanceRunner implements ServletSseLoadBalanceRunner {

    private String prefix;

    @Override
    public void run(Runnable runnable, URI uri, ConnectionLoadBalanceConcept concept) {
        Thread thread = new Thread(runnable);
        thread.setName(prefix + uri);
        thread.start();
    }
}
