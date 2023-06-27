package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息接收断言异常事件
 */
@Getter
@RequiredArgsConstructor
public class MessageReceivePredicateErrorEvent implements MessageEvent, ErrorEvent {

    private final Connection connection;

    private final Message message;

    private final Throwable error;
}
