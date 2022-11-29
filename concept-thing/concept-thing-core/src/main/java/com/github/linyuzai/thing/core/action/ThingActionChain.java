package com.github.linyuzai.thing.core.action;

public interface ThingActionChain {

    ThingActionChain next(ThingAction action);

    ThingActionInvocation invoke();
}
