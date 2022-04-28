package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.sql.Types;

public interface MessageHandler extends MessageReceiveEventListener {

    @Override
    default String getConnectionType() {
        return Connection.Type.CLIENT;
    }

    void onMessage(Message message, Connection connection);
}
