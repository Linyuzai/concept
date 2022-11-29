package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.InnerThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
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
    public ThingActionChain update(Object value) {
        return thing.actions().next(new InnerThingAction(() -> {
            Object oldValue = getValue();
            doUpdate(value);
            return new UpdateInvocation(this, oldValue, value);
        }));
    }

    protected void doUpdate(Object value) {
        //TODO valid
        this.value = value;
    }
}
