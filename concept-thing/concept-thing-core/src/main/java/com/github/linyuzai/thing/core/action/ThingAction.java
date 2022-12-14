package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.event.ThingEvent;

import java.util.function.Supplier;

public interface ThingAction {

    String ADD = "ThingAction::ADD";

    String REMOVE = "ThingAction::REMOVE";

    String VALUE_UPDATE = "ThingAction::VALUE_UPDATE";

    ThingActionExecution execute();

    //ThingActionChain chain();

    static ThingAction create(Supplier<Supplier<ThingEvent>> supplier) {
        return () -> {
            Supplier<ThingEvent> event = supplier.get();
            return event::get;
        };
    }


}
