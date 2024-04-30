package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息发送成功事件。
 * <p>
 * Event will be published when message sending is successful.
 */
@Getter
@RequiredArgsConstructor
public class MessageSendSuccessEvent extends TimestampEvent implements MessageEvent {

    private final Connection connection;

    private final Message message;
}
