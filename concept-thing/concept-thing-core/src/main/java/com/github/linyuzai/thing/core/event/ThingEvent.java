package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Thing;

public interface ThingEvent {

    Thing getThing();

    default void publish() {
        getThing().publish(this);
    }
}
