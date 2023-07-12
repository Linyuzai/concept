package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

import java.util.Map;

/**
 * 连接工厂的抽象类。
 * <p>
 * Abstract connection factory.
 */
public abstract class AbstractConnectionFactory<C extends Connection>
        extends AbstractScoped implements ConnectionFactory {

    /**
     * 创建连接，设置类型为客户端，设置初始元数据。
     * <p>
     * Create connection, set client type and init metadata.
     */
    @Override
    public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        AbstractConnection connection = doCreate(o, concept);
        connection.setType(Connection.Type.CLIENT);
        connection.addMetadata(metadata);
        return connection;
    }

    /**
     * 创建连接。
     * <p>
     * Create connection.
     */
    protected abstract AbstractConnection doCreate(Object o, ConnectionLoadBalanceConcept concept);
}
