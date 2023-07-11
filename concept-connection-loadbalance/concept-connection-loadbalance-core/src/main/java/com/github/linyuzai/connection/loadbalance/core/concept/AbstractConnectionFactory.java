package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

import java.util.Map;

/**
 * 连接工厂的抽象类
 *
 * @param <C> 连接类
 */
public abstract class AbstractConnectionFactory<C extends Connection>
        extends AbstractScoped implements ConnectionFactory {

    @Override
    public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        AbstractConnection connection = doCreate(o, concept);
        connection.setType(Connection.Type.CLIENT);
        connection.addMetadata(metadata);
        return connection;
    }

    protected abstract AbstractConnection doCreate(Object o, ConnectionLoadBalanceConcept concept);
}
