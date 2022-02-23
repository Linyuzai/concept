package com.github.linyuzai.connection.loadbalance.core.message;

public interface MessageFactory {

    Message create(Object message);
}
