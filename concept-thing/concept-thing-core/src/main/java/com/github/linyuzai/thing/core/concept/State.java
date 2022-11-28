package com.github.linyuzai.thing.core.concept;

public interface State extends IdAndKey {

    Label getLabel();

    Thing getThing();

    <T> T getValue();

    void update(Object value);

    interface Modifiable extends IdAndKey.Modifiable {

        void setLabel(Label label);

        void setThing(Thing thing);

        void setValue(Object value);
    }
}
