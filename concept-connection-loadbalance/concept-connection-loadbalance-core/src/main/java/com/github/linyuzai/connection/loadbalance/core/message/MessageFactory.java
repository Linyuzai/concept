package com.github.linyuzai.connection.loadbalance.core.message;

/**
 * 消息工厂
 */
public interface MessageFactory {

    boolean support(Object message);

    Message create(Object message);
}
