package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Map;

public interface ConnectionFactory {

    boolean support(Object o, Map<String, String> metadata);

    Connection create(Object o, Map<String, String> metadata);
}
