package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

import java.util.Collection;

/**
 * 连接选择器
 * <p>
 * 通过消息来筛选需要发送该消息的连接
 */
public interface ConnectionSelector extends Scoped {

    @Override
    default boolean support(String scope) {
        return true;
    }

    /**
     * 是否支持该消息
     *
     * @param message 消息
     * @return 是否支持
     */
    boolean support(Message message);

    /**
     * 选择连接
     *
     * @param message    消息
     * @param repository 连接仓库
     * @param concept    {@link ConnectionLoadBalanceConcept}
     * @return 需要发送该消息的连接
     */
    Collection<Connection> select(Message message, ConnectionRepository repository, ConnectionLoadBalanceConcept concept);
}
