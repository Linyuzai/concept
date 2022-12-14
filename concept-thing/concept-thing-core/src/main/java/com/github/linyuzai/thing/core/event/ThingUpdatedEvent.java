package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;

@Getter
public class ThingUpdatedEvent extends AbstractThingEvent {

    private final Object update;

    private final Object oldValue;

    private final Object newValue;

    public ThingUpdatedEvent(ThingContext context, String action, Object executor, Object update, Object oldValue, Object newValue) {
        super(context, action, executor);
        this.update = update;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
