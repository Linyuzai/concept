package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEvent;
import com.github.linyuzai.thing.core.event.ThingEvents;
import com.github.linyuzai.thing.core.util.ListWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ThingActionExecutions implements ListWrapper<ThingActionExecution>, ThingActionExecution {

    @Getter
    private final ThingContext context;

    private final List<ThingActionExecution> executions;

    @Override
    public ThingEvent toEvent() {
        List<ThingEvent> events = executions.stream()
                .map(ThingActionExecution::toEvent)
                .collect(Collectors.toList());
        return new ThingEvents(context, events);
    }

    @Override
    public List<ThingActionExecution> unwrap() {
        return executions;
    }
}
