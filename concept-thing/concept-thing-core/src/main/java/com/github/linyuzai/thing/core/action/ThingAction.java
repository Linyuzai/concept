package com.github.linyuzai.thing.core.action;

public interface ThingAction {

    ThingActionPerformance perform();

    ThingActionChain chain();
}
