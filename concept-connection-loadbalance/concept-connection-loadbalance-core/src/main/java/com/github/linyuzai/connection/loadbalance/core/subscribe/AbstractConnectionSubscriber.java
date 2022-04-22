package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class AbstractConnectionSubscriber implements ConnectionSubscriber {

    private final String hostKey = "concept-connection-host";

    private final String portKey = "concept-connection-port";

    private final String protocol;

    public AbstractConnectionSubscriber() {
        this("ws");
    }

    public AbstractConnectionSubscriber(String protocol) {
        this.protocol = protocol;
    }


    public String getHost(ConnectionServer server) {
        String host = server.getMetadata().get(getHostKey());
        if (host == null || host.isEmpty()) {
            return server.getHost();
        }
        return host;
    }

    public int getPort(ConnectionServer server) {
        String port = server.getMetadata().get(getPortKey());
        if (port == null || port.isEmpty()) {
            return server.getPort();
        }
        try {
            return Integer.parseInt(port);
        } catch (Throwable ignore) {
            return server.getPort();
        }
    }

    @SneakyThrows
    public URI getUri(ConnectionServer server) {
        return new URI(getProtocol() + "://" + getHost(server) + ":" + getPort(server) +
                getEndpointPrefix() + getType());
    }

    @Deprecated
    public Map<String, String> newMetadata(ConnectionServer server) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ProxyMarker.FLAG, getType());
        metadata.put(ConnectionServer.INSTANCE_ID, server.getInstanceId());
        return metadata;
    }

    public abstract String getEndpointPrefix();

    public abstract String getType();
}
