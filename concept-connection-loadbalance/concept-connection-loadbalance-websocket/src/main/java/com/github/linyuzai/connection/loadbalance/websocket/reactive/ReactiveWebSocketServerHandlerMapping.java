package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointConfigurer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactiveWebSocketServerHandlerMapping extends SimpleUrlHandlerMapping {

    public ReactiveWebSocketServerHandlerMapping(WebSocketLoadBalanceConcept concept, List<DefaultEndpointConfigurer> configurers) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put(WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX + "**", new ReactiveWebSocketServerHandler(concept));
        setUrlMap(map);
        setOrder(100);
        configurers.forEach(it -> it.configure(this));
    }
}
