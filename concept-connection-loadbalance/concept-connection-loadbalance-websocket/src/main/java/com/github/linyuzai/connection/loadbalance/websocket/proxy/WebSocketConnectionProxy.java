package com.github.linyuzai.connection.loadbalance.websocket.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.proxy.AbstractConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.standard.StandardWebSocketClientEndpoint;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class WebSocketConnectionProxy extends AbstractConnectionProxy {

    public static final String ENDPOINT_PREFIX = "/concept-ws-proxy/";

    private final String TYPE = "standard";

    private final String protocol;

    public WebSocketConnectionProxy() {
        this("ws");
    }

    public WebSocketConnectionProxy(String protocol) {
        this.protocol = protocol;
    }

    @SneakyThrows
    @Override
    public Connection proxy(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI(protocol + "://" + getHost(server) + ":" + getPort(server) + ENDPOINT_PREFIX + TYPE);
        Session session = container.connectToServer(StandardWebSocketClientEndpoint.class, uri);
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ConnectionProxy.FLAG, TYPE);
        metadata.put(ConnectionServer.INSTANCE_ID, server.getInstanceId());
        return concept.create(session, metadata);
    }
}
