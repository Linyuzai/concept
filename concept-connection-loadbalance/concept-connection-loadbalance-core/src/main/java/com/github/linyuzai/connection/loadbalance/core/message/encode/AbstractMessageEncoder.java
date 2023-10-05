package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PooledMessage;

public abstract class AbstractMessageEncoder implements MessageEncoder {

    @Override
    public Object encode(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
        if (message instanceof PooledMessage) {
            return ((PooledMessage) message).pooled(connection, msg -> doEncode(msg, connection, concept));
        }
        return doEncode(message, connection, concept);
    }

    public abstract Object doEncode(Message message, Connection connection, ConnectionLoadBalanceConcept concept);
}
