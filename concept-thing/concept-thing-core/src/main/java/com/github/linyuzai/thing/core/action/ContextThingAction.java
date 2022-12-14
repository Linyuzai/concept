package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Deprecated
@Getter
@RequiredArgsConstructor
public abstract class ContextThingAction implements ThingAction {

    private final ThingContext context;

    public ThingActionChain chain() {
        return getContext().actions().next(this);
    }

    public static ThingAction of(ThingContext context, Supplier<Supplier<ThingEvent>> supplier) {
        return new ContextThingAction(context) {

            @Override
            public ThingActionExecution execute() {
                Supplier<ThingEvent> event = supplier.get();
                return event::get;
            }
        };
    }
}
