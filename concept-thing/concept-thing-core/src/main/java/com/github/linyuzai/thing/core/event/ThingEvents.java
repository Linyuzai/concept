package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Thing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ThingEvents implements ThingEvent {

    private final Thing thing;

    private final List<ThingEvent> events;

    @Override
    public void publish() {
        thing.publish(this);
    }
}
