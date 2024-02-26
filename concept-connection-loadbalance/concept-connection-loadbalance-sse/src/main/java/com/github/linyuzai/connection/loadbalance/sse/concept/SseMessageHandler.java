package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.message.MessageHandler;

/**
 * SSE 消息处理器。
 * <p>
 * SSE message handler.
 */
public interface SseMessageHandler extends MessageHandler, SseEventListener {

}
