package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Attribute;

public interface AttributeAddedEvent extends ThingEvent {

    Attribute getAttribute();
}
