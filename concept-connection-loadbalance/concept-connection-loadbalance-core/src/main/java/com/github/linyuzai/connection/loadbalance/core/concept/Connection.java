package com.github.linyuzai.connection.loadbalance.core.concept;

public interface Connection {

    String host();

    int port();

    void send(Object message);
}
