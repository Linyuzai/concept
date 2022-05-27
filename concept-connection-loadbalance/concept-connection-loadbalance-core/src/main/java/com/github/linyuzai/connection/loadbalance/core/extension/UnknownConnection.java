package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import lombok.Getter;

/**
 * 未知连接
 * <p>
 * 用于表示在连接仓库中未找到的连接
 */
@Getter
public class UnknownConnection extends AbstractConnection {

    private final Object id;

    public UnknownConnection(Object id, String type, ConnectionLoadBalanceConcept concept) {
        super(type);
        setConcept(concept);
        this.id = id;
    }

    @Override
    public void ping(PingMessage ping) {

    }

    @Override
    public void pong(PongMessage pong) {

    }

    @Override
    public void doSend(Object message) {

    }

    @Override
    public void close(String reason) {

    }

    @Override
    public void doClose(Object reason) {

    }

    @Override
    public Object getCloseReason(int code, String reason) {
        return null;
    }
}
