package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.concept.Thing;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ThingActionChainFactoryImpl implements ThingActionChainFactory {

    private final List<ThingActionInvoker> invokers;

    @Override
    public ThingActionChain create(Thing thing) {
        return new ThingActionChainImpl(thing, invokers);
    }
}
