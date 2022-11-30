package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ThingEvents implements ThingEvent {

    private final ThingContext context;

    private final List<ThingEvent> events;

    @Override
    public void publish() {
        context.publish(this);
    }
}