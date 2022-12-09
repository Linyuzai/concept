package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingAction;

public interface Attribute extends Identify {

    Label getLabel();

    void setLabel(Label label);

    Thing getThing();

    void setThing(Thing thing);

    <T> T getValue();

    void setValue(Object value);

    ThingAction update(Object value);
}
