package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.Map;

public interface Message {

    String BROADCAST = "concept-connection-broadcast";

    String FORWARD = "concept-connection-forward";

    String HIT = "concept-connection-hit";

    Map<String, String> getHeaders();

    <T> T getPayload();
}
