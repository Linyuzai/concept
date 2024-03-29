package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.util.ListWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ThingEvents implements ListWrapper<ThingEvent>, ThingEvent {

    @Getter
    private final ThingContext context;

    private final List<ThingEvent> events;

    @Override
    public void publish() {
        context.publish(this);
    }

    @Override
    public List<ThingEvent> unwrap() {
        return events;
    }
}
