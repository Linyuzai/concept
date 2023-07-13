package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.Getter;

/**
 * 时间戳事件。
 * <p>
 * Event has a timestamp.
 */
@Getter
public class TimestampEvent {

    private final long timestamp = System.currentTimeMillis();
}
