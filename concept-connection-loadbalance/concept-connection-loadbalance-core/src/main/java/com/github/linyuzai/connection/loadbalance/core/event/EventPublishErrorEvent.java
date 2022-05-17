package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventPublishErrorEvent implements ErrorEvent {

    private final Object event;

    private final Throwable error;
}
