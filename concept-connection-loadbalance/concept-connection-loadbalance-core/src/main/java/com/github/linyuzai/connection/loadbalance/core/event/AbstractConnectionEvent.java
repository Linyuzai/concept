package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 连接事件抽象类
 */
@Getter
@AllArgsConstructor
public class AbstractConnectionEvent implements ConnectionEvent {

    private Connection connection;
}
