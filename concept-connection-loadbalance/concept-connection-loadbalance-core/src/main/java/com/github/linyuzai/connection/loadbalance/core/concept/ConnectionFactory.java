package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Map;

/**
 * 连接工厂
 * <p>
 * 用于将底层连接包装成 {@link Connection} 实例
 */
public interface ConnectionFactory {

    /**
     * 是否支持
     *
     * @param o        底层连接
     * @param metadata 元数据
     * @return 是否支持
     */
    boolean support(Object o, Map<Object, Object> metadata);

    /**
     * 创建 {@link Connection} 对象
     *
     * @param o        底层连接
     * @param metadata 元数据
     * @param concept  {@link ConnectionLoadBalanceConcept}
     * @return {@link Connection} 实例
     */
    Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept);
}
