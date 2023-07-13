package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息转发事件
 */
@Getter
@RequiredArgsConstructor
public class MessageForwardEvent extends TimestampEvent implements MessageEvent {

    private final Connection connection;

    private final Message message;
}
