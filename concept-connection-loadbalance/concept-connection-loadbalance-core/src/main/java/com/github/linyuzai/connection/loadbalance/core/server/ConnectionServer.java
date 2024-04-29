package com.github.linyuzai.connection.loadbalance.core.server;

import java.net.URI;
import java.util.Map;

/**
 * 连接服务。
 * <p>
 * A server can be connected.
 */
public interface ConnectionServer {

    String LB_HOST_PORT = "lbHostPort";

    static String url(ConnectionServer server) {
        if (server == null) {
            return null;
        }
        return server.getHost() + ":" + server.getPort();
    }

    String getInstanceId();

    String getServiceId();

    String getHost();

    int getPort();

    Map<String, String> getMetadata();

    URI getUri();

    String getScheme();

    boolean isSecure();
}
