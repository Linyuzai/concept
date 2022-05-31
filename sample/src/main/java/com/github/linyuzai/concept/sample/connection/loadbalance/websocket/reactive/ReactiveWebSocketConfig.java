package com.github.linyuzai.concept.sample.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketLoadBalanceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class ReactiveWebSocketConfig {

    //@Bean
    /*public HandlerMapping handlerMapping(WebSocketLoadBalanceConcept concept) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/echo/**", new ReactiveWebSocketLoadBalanceHandler(concept));
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        return mapping;
    }*/
}
