package com.github.linyuzai.connection.loadbalance.core.message;

/**
 * ping 消息。
 * <p>
 * Ping message.
 */
public interface PingMessage extends Message {

    @Override
    byte[] getPayload();
}
