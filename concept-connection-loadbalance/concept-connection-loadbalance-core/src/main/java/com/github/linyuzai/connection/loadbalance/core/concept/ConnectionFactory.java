package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Map;

public interface ConnectionFactory {

    boolean support(Object o, Map<Object, Object> metadata);

    Connection create(Object o, Map<Object, Object> metadata);
}
