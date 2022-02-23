package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Collection;

public interface ConnectionSelector {

    Connection select(Collection<Connection> connections);
}
