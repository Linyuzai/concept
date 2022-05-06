package com.github.linyuzai.connection.loadbalance.core.server;

import java.net.URI;
import java.util.Map;

public interface ConnectionServer {

    String getInstanceId();

    String getServiceId();

    String getHost();

    int getPort();

    Map<String, String> getMetadata();

    URI getUri();

    String getScheme();

    boolean isSecure();
}
