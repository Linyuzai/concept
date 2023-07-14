package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 消息编解码适配器。
 * <p>
 * Adapter of codec for message.
 */
public interface MessageCodecAdapter extends Scoped {

    /**
     * 根据连接类型获得消息编码器。
     * <p>
     * Get encoder for message by connection's type.
     */
    MessageEncoder getMessageEncoder(String type, MessageEncoder encoder);

    /**
     * 根据连接类型获得消息解码器。
     * <p>
     * Get decoder for message by connection's type.
     */
    MessageDecoder getMessageDecoder(String type, MessageDecoder decoder);
}
