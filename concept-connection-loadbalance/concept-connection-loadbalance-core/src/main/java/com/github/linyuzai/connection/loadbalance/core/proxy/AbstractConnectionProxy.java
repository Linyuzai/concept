package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractConnectionProxy implements ConnectionProxy {

    private final String hostKey = "concept-connection-host";

    private final String portKey = "concept-connection-port";

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
}
