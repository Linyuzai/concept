package com.github.linyuzai.connection.loadbalance.core.message.sender;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.util.Collection;

/**
 * 默认的消息发送器。
 * <p>
 * Default message sender.
 */
public class DefaultMessageSender implements MessageSender {

    @Override
    public void send(Collection<? extends Runnable> senders, ConnectionLoadBalanceConcept concept) {
        senders.forEach(Runnable::run);
    }
}
