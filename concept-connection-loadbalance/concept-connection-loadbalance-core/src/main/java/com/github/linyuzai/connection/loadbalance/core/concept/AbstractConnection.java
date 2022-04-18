package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class AbstractConnection implements Connection, ConnectionLoadBalanceConceptAware {

    private final Map<String, String> metadata;

    private ConnectionLoadBalanceConcept concept;

    @Override
    public void setConnectionLoadBalanceConcept(ConnectionLoadBalanceConcept concept) {
        this.concept = concept;
    }

    @Override
    public void send(Message message) {
        MessageEncoder encoder = getConcept().getMessageEncoder();
        byte[] bytes = encoder.encode(message);
        doSend(bytes);
    }

    public abstract void doSend(byte[] bytes);
}
