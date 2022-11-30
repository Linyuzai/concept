package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractAttributes extends AbstractContainer<Attribute> implements Attributes, Attributes.Modifiable {

    private Thing thing;

    @Override
    protected ThingContext getContext() {
        return thing.getContext();
    }

    @Override
    protected void onPrepare(Attribute add) {
        if (add instanceof Attribute.Modifiable) {
            if (add.getThing() == null) {
                ((Attribute.Modifiable) add).setThing(thing);
            }
        }
    }

    @Override
    protected void onValid(Attribute add) {
        if (add.getLabel() == null) {

        }
        if (!thing.equals(add.getThing())) {

        }
    }
}
