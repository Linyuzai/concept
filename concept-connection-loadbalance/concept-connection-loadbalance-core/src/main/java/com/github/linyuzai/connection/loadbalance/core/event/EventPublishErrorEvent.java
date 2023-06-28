package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件发布异常事件
 */
@Getter
@AllArgsConstructor
public class EventPublishErrorEvent extends TimestampEvent implements ErrorEvent {

    private final Object event;

    private final Throwable error;
}
