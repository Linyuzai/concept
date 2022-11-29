package com.github.linyuzai.thing.core.action.inner;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.action.ThingActionInvoker;
import com.github.linyuzai.thing.core.context.ThingContext;

import java.util.function.Supplier;

public class InnerThingActionInvoker implements ThingActionInvoker {

    @Override
    public boolean support(ThingAction action, ThingContext context) {
        return action instanceof InnerThingAction;
    }

    @Override
    public ThingActionInvocation invoke(ThingAction action, ThingContext context) {
        InnerThingAction ita = (InnerThingAction) action;
        Supplier<ThingActionInvocation> supplier = ita.getSupplier();
        return supplier.get();
    }
}
