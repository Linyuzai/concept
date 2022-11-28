package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.concept.Thing;

public interface ThingActionInvoker {

    boolean support(ThingAction action, Thing thing);

    ThingActionInvocation invoke(ThingAction action, Thing thing);
}
