package com.github.linyuzai.connection.loadbalance.core.message;

/**
 * pong 消息。
 * <p>
 * Pong message.
 */
public interface PongMessage extends Message {

    @Override
    byte[] getPayload();
}
