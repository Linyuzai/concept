package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ProtocolConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;

/**
 * ws 连接订阅者。
 * <p>
 * ws connection subscriber.
 */
@Getter
@Setter
public abstract class WebSocketConnectionSubscriber<T extends WebSocketConnection>
        extends ProtocolConnectionSubscriber<T> {

    private String protocol = "ws";

    private String endpoint = WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT;

    public abstract String getType();
}
