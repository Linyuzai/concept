package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.sse.concept.DefaultSseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * Servlet SSE 连接订阅器工厂。
 * <p>
 * Servlet SSE connection subscriber factory.
 */
@Getter
@Setter
public class ServletSseConnectionSubscriberFactory extends SseConnectionSubscriberFactory<ServletSubscriberSseConnection> {

    private SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private ServletSseLoadBalanceRunner servletSseLoadBalanceRunner;

    @Override
    public SseConnectionSubscriber<ServletSubscriberSseConnection> doCreate(String scope) {
        return new ServletSseConnectionSubscriber(sseIdGenerator, servletSseLoadBalanceRunner);
    }
}
