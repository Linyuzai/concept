package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class ReactiveLoadBalanceWebSocketHandlerMapping extends SimpleUrlHandlerMapping {

    public ReactiveLoadBalanceWebSocketHandlerMapping(WebSocketLoadBalanceConcept concept) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put(WebSocketLoadBalanceConcept.ENDPOINT_PREFIX + "**", new ReactiveLoadBalanceWebSocketHandler(concept));
        setUrlMap(map);
        setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
