package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.event.ThingUpdatedEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractAttribute extends AbstractIdentify implements Attribute {

    private Label label;

    private Thing thing;

    private Object value;

    @Override
    public ThingAction update(Object value) {
        return ThingAction.create(() -> {
            Object oldValue = getValue();
            doUpdate(value);
            return () -> new ThingUpdatedEvent(getContext(), ThingAction.VALUE_UPDATE, this, this, oldValue, value);
        });
    }

    protected void doUpdate(Object value) {
        //TODO valid
        setValue(value);
    }
}
