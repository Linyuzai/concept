package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

import java.util.Collection;

/**
 * 不进行过滤的选择器
 */
public class AllSelector extends AbstractConnectionSelector {

    @Override
    public boolean support(Message message) {
        return true;
    }

    @Override
    public Collection<Connection> doSelect(Message message, Collection<Connection> connections) {
        return connections;
    }
}
