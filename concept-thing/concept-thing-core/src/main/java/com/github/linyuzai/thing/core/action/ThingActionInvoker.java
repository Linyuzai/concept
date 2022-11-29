package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;

public interface ThingActionInvoker {

    boolean support(ThingAction action, ThingContext context);

    ThingActionInvocation invoke(ThingAction action, ThingContext context);
}
