package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;

@Getter
public class ThingAddedEvent extends AbstractThingEvent {

    private final Object added;

    public ThingAddedEvent(ThingContext context, String action, Object executor, Object added) {
        super(context, action, executor);
        this.added = added;
    }
}
