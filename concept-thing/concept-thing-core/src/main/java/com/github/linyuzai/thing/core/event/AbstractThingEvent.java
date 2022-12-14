package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractThingEvent implements ThingEvent {

    private final ThingContext context;

    private final String action;

    private final Object executor;

    @Override
    public void publish() {
        context.publish(this);
    }
}
