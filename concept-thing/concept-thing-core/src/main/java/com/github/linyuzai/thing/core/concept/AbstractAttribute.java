package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ContextThingAction;
import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.event.AttributeUpdatedEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractAttribute implements Attribute, Attribute.Modifiable {

    private String id;

    private String key;

    private Label label;

    private Thing thing;

    private Object value;

    @Override
    public ThingAction update(Object value) {
        return ContextThingAction.of(thing.getContext(), () -> {
            Object oldValue = getValue();
            doUpdate(value);
            return () -> new AttributeUpdatedEvent(this, oldValue, value);
        });
    }

    protected void doUpdate(Object value) {
        //TODO valid
        this.value = value;
    }
}
