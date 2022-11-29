package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingAction;

public interface Attribute extends IdAndKey {

    Label getLabel();

    Thing getThing();

    <T> T getValue();

    ThingAction update(Object value);

    interface Modifiable extends IdAndKey.Modifiable {

        void setLabel(Label label);

        void setThing(Thing thing);

        void setValue(Object value);
    }
}
