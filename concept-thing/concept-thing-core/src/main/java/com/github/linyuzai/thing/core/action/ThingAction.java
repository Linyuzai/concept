package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEvent;

import java.util.function.Supplier;

public interface ThingAction {

    ThingActionPerformance perform();

    ThingActionChain chain();
}
