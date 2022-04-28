package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.PongMessageEvent;

import java.nio.ByteBuffer;

public class AutoPongHandler implements ConnectionEventListener {

    @Override
    public void onEvent(Object event) {
        //TODO redefine
        if (event instanceof Message && isPingMessage((Message) event)) {
            Connection connection = ((PongMessageEvent) event).getConnection();
            connection.send(createPongMessage((Message) event));
        }
    }

    public boolean isPingMessage(Message message) {
        return message instanceof PingMessage;
    }

    public Message createPongMessage(Message ping) {
        return new BinaryPongMessage(ByteBuffer.allocate(0));
    }
}
