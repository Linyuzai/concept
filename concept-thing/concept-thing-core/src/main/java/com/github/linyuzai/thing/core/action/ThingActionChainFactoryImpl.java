package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ThingActionChainFactoryImpl implements ThingActionChainFactory {

    private final List<ThingActionInvoker> invokers;

    @Override
    public ThingActionChain create(ThingContext context) {
        return new ThingActionChainImpl(context, invokers);
    }
}
