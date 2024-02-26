package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Reactive SSE 连接订阅器工厂。
 * <p>
 * Reactive SSE connection subscriber factory.
 */
@Getter
@Setter
public class ReactiveSseConnectionSubscriberFactory extends SseConnectionSubscriberFactory<ReactiveSseConnection> {

    private ReactiveSseClientFactory sseClientFactory;

    @Override
    public SseConnectionSubscriber<ReactiveSseConnection> doCreate(String scope) {
        return new ReactiveSseConnectionSubscriber(sseClientFactory);
    }
}
