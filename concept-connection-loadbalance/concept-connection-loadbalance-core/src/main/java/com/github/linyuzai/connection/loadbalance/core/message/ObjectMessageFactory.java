package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

/**
 * {@link ObjectMessage} 的消息工厂。
 * <p>
 * Factory to create {@link ObjectMessage}
 */
public class ObjectMessageFactory implements MessageFactory {

    @Override
    public boolean support(Object message, ConnectionLoadBalanceConcept concept) {
        return true;
    }

    @Override
    public Message create(Object message, ConnectionLoadBalanceConcept concept) {
        return new ObjectMessage(message);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
