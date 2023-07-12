package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息丢弃事件
 */
@Getter
@RequiredArgsConstructor
public class MessageDiscardEvent extends TimestampEvent implements ConnectionEvent, MessageEvent {

    private final Connection connection;

    private final Message message;
}
