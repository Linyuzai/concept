package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息编码异常事件
 */
@Getter
@RequiredArgsConstructor
public class MessageEncodeErrorEvent implements ConnectionEvent, ErrorEvent {

    private final Connection connection;

    private final Throwable error;
}
