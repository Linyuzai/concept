package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.message.MessageHandler;

/**
 * Netty 消息处理器。
 * <p>
 * Netty message handler.
 */
public interface NettyMessageHandler extends MessageHandler, NettyEventListener {

}
