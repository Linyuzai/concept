package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.Map;

public interface Message {
    String FORWARD = "concept-connection-forward";

    Map<String, String> getHeaders();

    <T> T getPayload();
}
