package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;

public interface ThingAction {

    ThingContext getContext();

    default ThingActionChain chain() {
        if (this instanceof ThingActionChain) {
            return (ThingActionChain) this;
        }
        return getContext().actions().next(this);
    }
}
