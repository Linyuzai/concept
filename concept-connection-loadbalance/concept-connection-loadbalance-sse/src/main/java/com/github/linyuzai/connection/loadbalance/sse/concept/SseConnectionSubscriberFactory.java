package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ProtocolConnectionSubscriberFactory;

/**
 * SSE 连接订阅者工厂抽象类。
 * <p>
 * Abstract class of SSE connection subscriber factory.
 */
public abstract class SseConnectionSubscriberFactory<T extends SseConnection>
        extends ProtocolConnectionSubscriberFactory<T> {

    public SseConnectionSubscriberFactory() {
        addScopes(SseScoped.NAME);
    }
}
