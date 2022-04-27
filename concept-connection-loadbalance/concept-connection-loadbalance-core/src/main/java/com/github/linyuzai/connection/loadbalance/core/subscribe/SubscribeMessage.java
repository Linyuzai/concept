package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public class SubscribeMessage extends AbstractMessage<ConnectionServer> {

    public SubscribeMessage(ConnectionServer payload) {
        super(payload);
    }
}
