package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.inner.InnerThingAction;
import com.github.linyuzai.thing.core.action.inner.InnerThingActionInvocation;
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
        return new InnerThingAction(thing.getContext(), () -> {
            Object oldValue = getValue();
            doUpdate(value);
            return new InnerThingActionInvocation(() -> new AttributeUpdatedEvent(this, oldValue, value));
        });
    }

    protected void doUpdate(Object value) {
        //TODO valid
        this.value = value;
    }
}
