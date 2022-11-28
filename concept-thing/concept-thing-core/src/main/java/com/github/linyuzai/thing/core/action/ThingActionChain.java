package com.github.linyuzai.thing.core.action;

public interface ThingActionChain {

    ThingActionChain action(ThingAction action);

    void invoke();
}
