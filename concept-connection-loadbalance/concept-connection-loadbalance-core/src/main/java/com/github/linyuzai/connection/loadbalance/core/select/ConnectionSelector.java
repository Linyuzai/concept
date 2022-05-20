package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;

import java.util.Collection;

public interface ConnectionSelector {

    boolean support(Message message);

    Collection<Connection> select(Message message, ConnectionRepository repository, ConnectionLoadBalanceConcept concept);
}
