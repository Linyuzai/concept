package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息发送异常事件。
 * <p>
 * Event will be published when message sending is failed.
 */
@Getter
@RequiredArgsConstructor
public class MessageSendErrorEvent extends TimestampEvent implements MessageEvent, ErrorEvent {

    private final Connection connection;

    private final Message message;

    private final Throwable error;
}
