package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Attribute;

public interface AttributeUpdatedEvent extends ThingEvent {

    Attribute getAttribute();

    Object getOldValue();

    Object getNewValue();
}
