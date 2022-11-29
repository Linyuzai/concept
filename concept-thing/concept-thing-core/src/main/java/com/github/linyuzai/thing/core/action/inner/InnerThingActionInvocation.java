package com.github.linyuzai.thing.core.action.inner;

import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class InnerThingActionInvocation implements ThingActionInvocation {

    private final Supplier<ThingEvent> supplier;

    @Override
    public ThingEvent toEvent() {
        return supplier.get();
    }
}
