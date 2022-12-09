package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ContextThingAction;
import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.event.AttributeUpdatedEvent;

public abstract class AbstractAttribute implements Attribute {

    @Override
    public ThingAction update(Object value) {
        return ContextThingAction.of(getThing().getContext(), () -> {
            Object oldValue = getValue();
            doUpdate(value);
            return () -> new AttributeUpdatedEvent(this, oldValue, value);
        });
    }

    protected void doUpdate(Object value) {
        //TODO valid
        setValue(value);
    }
}
