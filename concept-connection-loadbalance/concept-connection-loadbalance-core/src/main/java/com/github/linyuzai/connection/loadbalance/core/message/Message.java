package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.Map;

public interface Message {

    Map<String, Object> headers();

    byte[] bytes();
}
