package com.github.linyuzai.connection.loadbalance.websocket.standard;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.proxy.AbstractConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class StandardWebSocketConnectionProxy extends AbstractConnectionProxy {

    public static final String ENDPOINT_PREFIX = "/concept-ws-standard-proxy/";

    public static final String TYPE = "-standard";

    private final String protocol;

    public StandardWebSocketConnectionProxy() {
        this("ws");
    }

    public StandardWebSocketConnectionProxy(String protocol) {
        this.protocol = protocol;
    }

    @SneakyThrows
    @Override
    public Connection proxy(ConnectionServer server) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String type = protocol + TYPE;
        URI uri = new URI(protocol + "://" + getHost(server) + ":" + getPort(server) + ENDPOINT_PREFIX + type);
        Session session = container.connectToServer(StandardWebSocketClientEndpoint.class, uri);
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ConnectionProxy.FLAG, TYPE);
        metadata.put(ConnectionServer.INSTANCE_ID, server.getInstanceId());
        return new WebSocketConnection(session, metadata);
    }
}
