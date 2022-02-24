package com.github.linyuzai.connection.loadbalance.core.message;

public interface MessageFactoryAdapter {

    MessageFactory getMessageFactory(Object message);
}
