package com.github.linyuzai.connection.loadbalance.core.message;

/**
 * {@link ObjectMessage} 的消息工厂
 */
public class ObjectMessageFactory implements MessageFactory {

    @Override
    public boolean support(Object message) {
        return true;
    }

    @Override
    public Message create(Object message) {
        return new ObjectMessage(message);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
