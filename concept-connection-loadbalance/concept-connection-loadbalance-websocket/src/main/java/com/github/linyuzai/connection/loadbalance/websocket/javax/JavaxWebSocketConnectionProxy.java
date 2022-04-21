package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.proxy.AbstractConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.proxy.ProxyMarker;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class JavaxWebSocketConnectionProxy extends AbstractConnectionProxy {

    public static final String TYPE = "standard";

    private static ConnectionLoadBalanceConcept concept;

    public static ConnectionLoadBalanceConcept getConcept() {
        return concept;
    }

    private final String protocol;

    public JavaxWebSocketConnectionProxy() {
        this("ws");
    }

    public JavaxWebSocketConnectionProxy(String protocol) {
        this.protocol = protocol;
    }

    @SneakyThrows
    @Override
    public Connection proxy(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        JavaxWebSocketConnectionProxy.concept = concept;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI(protocol + "://" + getHost(server) + ":" + getPort(server) + WebSocketLoadBalanceConcept.ENDPOINT_PREFIX + TYPE);
        Session session = container.connectToServer(JavaxWebSocketClientEndpoint.class, uri);
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ProxyMarker.FLAG, TYPE);
        metadata.put(ConnectionServer.INSTANCE_ID, server.getInstanceId());
        return concept.create(session, metadata);
    }


}
