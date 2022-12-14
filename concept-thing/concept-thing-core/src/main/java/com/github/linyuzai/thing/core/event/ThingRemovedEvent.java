package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;

@Getter
public class ThingRemovedEvent extends AbstractThingEvent {

    private final String id;

    private final Object removed;

    public ThingRemovedEvent(ThingContext context, String action, Object executor, String id, Object removed) {
        super(context, action, executor);
        this.id = id;
        this.removed = removed;
    }
}
