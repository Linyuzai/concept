package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SubscribeMessage extends AbstractMessage<ConnectionServer> {

    public SubscribeMessage(ConnectionServer payload) {
        setPayload(payload);
    }
}
