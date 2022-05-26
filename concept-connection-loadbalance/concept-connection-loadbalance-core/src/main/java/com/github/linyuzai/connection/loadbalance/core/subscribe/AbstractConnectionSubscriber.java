package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.function.Consumer;

/**
 * 来接订阅者的抽象类
 *
 * @param <Con>     连接类
 * @param <Concept> 概念类型
 */
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

    /**
     * 获得对应服务的连接 host
     * <p>
     * 如果手动定义了值则使用手动定义的
     * <p>
     * 否则使用服务的 host
     *
     * @param server 服务实例
     * @return 对应服务的连接 host
     */
    public String getHost(ConnectionServer server) {
        String host = server.getMetadata().get(getHostKey());
        if (host == null || host.isEmpty()) {
            return server.getHost();
        }
        return host;
    }

    /**
     * 获得对应服务的连接 port
     * <p>
     * 如果手动定义了值则使用手动定义的
     * <p>
     * 否则使用服务的 port
     *
     * @param server 服务实例
     * @return 对应服务的连接 port
     */
    public int getPort(ConnectionServer server) {
        String port = server.getMetadata().get(getPortKey());
        if (port == null || port.isEmpty()) {
            return server.getPort();
        }
        return Integer.parseInt(port);
    }

    /**
     * 拼接对应服务实例连接的 {@link URI}
     *
     * @param server 服务实例
     * @return 对应的 {@link URI}
     */
    @SneakyThrows
    public URI getUri(ConnectionServer server) {
        return new URI(getProtocol() + "://" + getHost(server) + ":" + getPort(server) +
                getEndpoint());
    }

    /**
     * 获得端点路径
     *
     * @return 端点路径
     */
    public abstract String getEndpoint();

    @Deprecated
    public abstract String getType();
}
