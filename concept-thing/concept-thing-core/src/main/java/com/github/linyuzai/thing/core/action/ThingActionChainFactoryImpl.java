package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;

public class ThingActionChainFactoryImpl implements ThingActionChainFactory {

    @Override
    public ThingActionChain create(ThingContext context) {
        return new ThingActionChainImpl(context);
    }
}
