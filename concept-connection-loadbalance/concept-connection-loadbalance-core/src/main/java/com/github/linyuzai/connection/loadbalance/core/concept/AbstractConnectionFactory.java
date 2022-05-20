package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Map;

/**
 * 连接工厂的抽象类
 *
 * @param <Con>     连接类型
 * @param <Concept> {@link ConnectionLoadBalanceConcept} 类型
 */
public abstract class AbstractConnectionFactory<Con extends Connection, Concept extends ConnectionLoadBalanceConcept> implements ConnectionFactory {

    @SuppressWarnings("unchecked")
    @Override
    public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return doCreate(o, metadata, (Concept) concept);
    }

    /**
     * 创建指定类型的连接
     *
     * @param o        各类连接
     * @param metadata 元数据
     * @param concept  {@link ConnectionLoadBalanceConcept}
     * @return 指定类型的 {@link Connection} 实例
     */
    public abstract Con doCreate(Object o, Map<Object, Object> metadata, Concept concept);
}
