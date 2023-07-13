package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息解码异常事件。
 * <p>
 * Event will be published when message decode error.
 */
@Getter
@RequiredArgsConstructor
public class MessageDecodeErrorEvent extends TimestampEvent implements ErrorEvent {

    private final Connection connection;

    private final Throwable error;
}
