package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 消息工厂
 */
public interface MessageFactory extends Scoped {

    boolean support(Object message);

    Message create(Object message);
}
