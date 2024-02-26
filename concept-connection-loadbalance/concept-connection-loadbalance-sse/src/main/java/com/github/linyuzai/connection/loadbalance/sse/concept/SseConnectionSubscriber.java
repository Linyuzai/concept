package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ProtocolConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;

/**
 * SSE 连接订阅者。
 * <p>
 * SSE connection subscriber.
 */
@Getter
@Setter
public abstract class SseConnectionSubscriber<T extends SseConnection>
        extends ProtocolConnectionSubscriber<T> {

    private String protocol = "http";

    @Override
    public String getEndpoint() {
        return SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT;
    }

    public abstract String getType();
}
