package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.concept.Thing;

public interface ThingActionChainFactory {

    ThingActionChain create(Thing thing);
}
