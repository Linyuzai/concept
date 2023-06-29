package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 消息编解码适配器
 * <p>
 * 统一管理编解码器，不然很混乱
 */
public interface MessageCodecAdapter extends Scoped {

    /**
     * 通过连接类型获得消息编码器
     *
     * @param type 连接类型
     * @return 消息编码器
     */
    MessageEncoder getMessageEncoder(String type, MessageEncoder encoder);

    /**
     * 通过连接类型获得消息解码器
     *
     * @param type 连接类型
     * @return 消息解码器
     */
    MessageDecoder getMessageDecoder(String type, MessageDecoder decoder);
}
