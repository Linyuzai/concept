package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;

/**
 * 连接订阅者工厂。
 * <p>
 * Factory of {@link ConnectionSubscriber}.
 */
public abstract class AbstractConnectionSubscriberFactory extends AbstractScopedFactory<ConnectionSubscriber>
        implements ConnectionSubscriberFactory {
}
