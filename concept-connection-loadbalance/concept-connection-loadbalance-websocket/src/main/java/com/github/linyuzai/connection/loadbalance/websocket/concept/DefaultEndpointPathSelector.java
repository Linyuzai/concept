package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.extension.PathSelector;

public class DefaultEndpointPathSelector extends PathSelector {

    public DefaultEndpointPathSelector() {
        super(WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX);
    }
}
