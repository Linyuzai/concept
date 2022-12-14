package com.github.linyuzai.thing.core.action;

public interface ThingActionChain extends ThingAction {

    ThingActionChain next(ThingAction action);
}
