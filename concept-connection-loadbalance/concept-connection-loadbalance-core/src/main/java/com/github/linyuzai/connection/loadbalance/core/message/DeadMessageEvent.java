package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 死亡消息事件。
 * 当该消息不会发送给任何连接时发布。
 * <p>
 * Event will be published when no connection to send.
 */
@Getter
@AllArgsConstructor
public class DeadMessageEvent extends TimestampEvent implements MessageEvent {

    private Message message;
}
