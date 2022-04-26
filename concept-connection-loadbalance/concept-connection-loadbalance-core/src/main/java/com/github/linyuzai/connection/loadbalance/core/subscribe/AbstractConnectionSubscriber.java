package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.function.Consumer;

@Getter
public abstract class AbstractConnectionSubscriber<Con extends Connection, Concept extends ConnectionLoadBalanceConcept>
        implements ConnectionSubscriber {

    private final String hostKey = "concept-connection-host";

    private final String portKey = "concept-connection-port";

    private final String protocol;

    public AbstractConnectionSubscriber(String protocol) {
        this.protocol = protocol;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void subscribe(ConnectionServer server, ConnectionLoadBalanceConcept concept, Consumer<? extends Connection> consumer) {
        doSubscribe(server, (Concept) concept, (Consumer<Con>) consumer);
    }

    public abstract void doSubscribe(ConnectionServer server, Concept concept, Consumer<Con> consumer);

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

    public abstract String getEndpointPrefix();

    public abstract String getType();
}
