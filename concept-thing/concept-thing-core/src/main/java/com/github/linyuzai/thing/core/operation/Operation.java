package com.github.linyuzai.thing.core.operation;

import com.github.linyuzai.thing.core.event.ThingEvent;

public interface Operation {

    default Operation merge(Operation operation) {
        return new ComposeOperation().merge(this).merge(operation);
    }

    default ThingEvent toEvent() {
        return convert(ThingEvent.class);
    }

    <T> T convert(Class<T> target);
}
