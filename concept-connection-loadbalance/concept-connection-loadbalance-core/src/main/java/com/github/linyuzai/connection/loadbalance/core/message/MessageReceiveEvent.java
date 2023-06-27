package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息接收事件
 */
@Getter
@RequiredArgsConstructor
public class MessageReceiveEvent implements ConnectionEvent, MessageEvent {

    private final Connection connection;

    private final Message message;
}
