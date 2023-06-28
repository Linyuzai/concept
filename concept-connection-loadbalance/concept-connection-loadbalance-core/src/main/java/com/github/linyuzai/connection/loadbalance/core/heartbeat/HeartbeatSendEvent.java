package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 心跳发送事件
 */
@Getter
@RequiredArgsConstructor
public class HeartbeatSendEvent extends TimestampEvent implements HeartbeatEvent, MessageEvent {

    private final Collection<Connection> connections;

    private final Message message;

    private final String connectionType;
}
