package com.github.linyuzai.connection.loadbalance.core.server;

import lombok.Data;

import java.net.URI;
import java.util.Map;

@Data
public class ConnectionServerImpl implements ConnectionServer {

    private String instanceId;

    private String serviceId;

    private String host;

    private int port;

    private Map<String, String> metadata;

    private URI uri;

    private String scheme;

    private boolean secure;
}
