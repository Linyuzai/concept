package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;

public interface ThingActionChainFactory {

    ThingActionChain create(ThingContext context);
}
