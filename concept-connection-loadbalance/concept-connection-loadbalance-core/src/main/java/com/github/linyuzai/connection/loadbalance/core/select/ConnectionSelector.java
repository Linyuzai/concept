package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

import java.util.Collection;

public interface ConnectionSelector {

    ConnectionSelector broadcast(boolean broadcast);

    boolean support(Message message);

    Connection select(Message message, Collection<Connection> clients, Collection<Connection> observables);
}
