package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 死亡消息事件
 * <p>
 * 当该消息不会发送给任何连接时发布
 */
@Getter
@AllArgsConstructor
public class DeadMessageEvent extends TimestampEvent implements MessageEvent {

    private Message message;
}
