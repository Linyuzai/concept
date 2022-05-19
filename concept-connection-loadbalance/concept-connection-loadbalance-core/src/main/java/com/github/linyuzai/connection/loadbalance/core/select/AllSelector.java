package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.extension.Connections;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

import java.util.Collection;

public class AllSelector extends AbstractConnectionSelector {

    @Override
    public boolean support(Message message) {
        return true;
    }

    @Override
    public Connection doSelect(Message message, Collection<Connection> connections) {
        return Connections.of(connections);
    }
}
