package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.Setter;

public class SubscribeMessage extends AbstractMessage {

    @Getter
    @Setter
    private ConnectionServer connectionServer;

    @SuppressWarnings("unchecked")
    @Override
    public ConnectionServer getPayload() {
        return connectionServer;
    }
}
