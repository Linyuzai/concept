package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.Getter;

@Getter
public class TimestampEvent {

    private final long timestamp = System.currentTimeMillis();
}
