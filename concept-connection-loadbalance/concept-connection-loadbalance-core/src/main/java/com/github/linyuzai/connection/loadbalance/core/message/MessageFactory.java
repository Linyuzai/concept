package com.github.linyuzai.connection.loadbalance.core.message;

public interface MessageFactory {

    boolean support(Object message);

    Message create(Object message);
}
