package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.exception.WebSocketLoadBalanceException;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.StandardWebSocketClient;
import reactor.core.publisher.FluxSink;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ReactiveWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber {

    @Override
    public void doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept, Consumer<Connection> consumer) {
        StandardWebSocketClient client = new StandardWebSocketClient();
        ReactiveWebSocketSubscriberHandler handler =
                new ReactiveWebSocketSubscriberHandler(concept, server, (session, sink) -> {
                    ReactiveWebSocketConnection connection =
                            new ReactiveWebSocketConnection(session, sink, Connection.Type.SUBSCRIBER);
                    connection.getMetadata().put(ConnectionServer.class, server);
                    setDefaultMessageEncoder(connection);
                    setDefaultMessageDecoder(connection);
                    consumer.accept(connection);
                });
        URI uri = getUri(server);
        client.execute(uri, handler).subscribe();
    }

    @Override
    public String getType() {
        return "reactive";
    }
}
