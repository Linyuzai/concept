package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Attribute;

public interface AttributeRemovedEvent extends ThingEvent {

    String getAttributeId();

    Attribute getAttribute();
}
