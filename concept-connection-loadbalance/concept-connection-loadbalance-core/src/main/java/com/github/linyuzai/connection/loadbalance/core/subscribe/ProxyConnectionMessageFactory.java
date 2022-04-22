package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public class ProxyConnectionMessageFactory implements MessageFactory {

    @Override
    public boolean support(Object message) {
        return message instanceof ConnectionServer;
    }

    @Override
    public Message create(Object message) {
        return new ProxyConnectionMessage((ConnectionServer) message);
    }
}
