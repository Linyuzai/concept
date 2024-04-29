package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.DefaultSseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * Reactive SSE 连接订阅器工厂。
 * <p>
 * Reactive SSE connection subscriber factory.
 */
@Getter
@Setter
public class ReactiveSseConnectionSubscriberFactory extends SseConnectionSubscriberFactory<ReactiveSubscriberSseConnection> {

    private SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private ReactiveSseClientFactory sseClientFactory;

    @Override
    public SseConnectionSubscriber<ReactiveSubscriberSseConnection> doCreate(String scope) {
        return new ReactiveSseConnectionSubscriber(sseIdGenerator, sseClientFactory);
    }
}
