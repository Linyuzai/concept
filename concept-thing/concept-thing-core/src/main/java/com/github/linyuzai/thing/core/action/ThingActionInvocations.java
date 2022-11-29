package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEvent;
import com.github.linyuzai.thing.core.event.ThingEvents;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ThingActionInvocations implements ThingActionInvocation {

    private final ThingContext context;

    private final List<ThingActionInvocation> invocations;

    @Override
    public ThingEvent toEvent() {
        List<ThingEvent> events = invocations.stream()
                .map(ThingActionInvocation::toEvent)
                .collect(Collectors.toList());
        return new ThingEvents(context, events);
    }
}
