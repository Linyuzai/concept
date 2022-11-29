package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Attribute;

public abstract class AttributeEvent implements ThingEvent {

    public abstract Attribute getAttribute();

    @Override
    public void publish() {
        getAttribute().getThing().publish(this);
    }
}
