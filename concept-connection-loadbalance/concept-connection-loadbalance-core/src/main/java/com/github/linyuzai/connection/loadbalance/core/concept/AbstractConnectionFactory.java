package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

/**
 * 连接工厂的抽象类
 *
 * @param <C> 连接类
 */
public abstract class AbstractConnectionFactory<C extends Connection>
        extends AbstractScoped implements ConnectionFactory {
}
