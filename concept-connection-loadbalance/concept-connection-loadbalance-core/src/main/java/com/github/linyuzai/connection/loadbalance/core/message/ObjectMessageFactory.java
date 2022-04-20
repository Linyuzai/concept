package com.github.linyuzai.connection.loadbalance.core.message;

public class ObjectMessageFactory implements MessageFactory {

    @Override
    public boolean support(Object message) {
        return true;
    }

    @Override
    public Message create(Object message) {
        return new ObjectMessage(message);
    }
}
