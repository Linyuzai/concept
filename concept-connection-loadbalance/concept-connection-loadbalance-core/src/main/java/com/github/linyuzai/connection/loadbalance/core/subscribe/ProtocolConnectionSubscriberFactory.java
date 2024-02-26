package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ProtocolConnectionSubscriberFactory<T extends Connection> extends AbstractConnectionSubscriberFactory {

    private String protocol;

    @Override
    public ConnectionSubscriber create(String scope) {
        ProtocolConnectionSubscriber<T> subscriber = doCreate(scope);
        if (protocol != null && !protocol.isEmpty()) {
            subscriber.setProtocol(protocol);
        }
        return subscriber;
    }

    public abstract ProtocolConnectionSubscriber<T> doCreate(String scope);
}
