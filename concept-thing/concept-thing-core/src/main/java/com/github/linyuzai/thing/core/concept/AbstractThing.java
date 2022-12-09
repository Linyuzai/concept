package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingActionChain;

public abstract class AbstractThing implements Thing {

    @Override
    public ThingActionChain actions() {
        return getContext().actions();
    }

    @Override
    public void publish(Object event) {
        getContext().publish(event);
    }

    @Override
    public <T> T create(Class<T> target) {
        return null;
    }
}
