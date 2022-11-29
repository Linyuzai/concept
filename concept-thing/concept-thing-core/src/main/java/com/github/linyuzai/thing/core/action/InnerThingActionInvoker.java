package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.concept.Thing;

import java.util.function.Supplier;

public class InnerThingActionInvoker implements ThingActionInvoker {

    @Override
    public boolean support(ThingAction action, Thing thing) {
        return action instanceof InnerThingAction;
    }

    @Override
    public ThingActionInvocation invoke(ThingAction action, Thing thing) {
        InnerThingAction ita = (InnerThingAction) action;
        Supplier<ThingActionInvocation> supplier = ita.getSupplier();
        return supplier.get();
    }
}
