package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.concept.ThingImpl;
import com.github.linyuzai.thing.core.container.Things;
import com.github.linyuzai.thing.core.container.ThingsImpl;

public class ThingFactoryImpl implements ThingFactory {

    @Override
    public Thing create() {
        return new ThingImpl();
    }

    @Override
    public Things createContainer() {
        return new ThingsImpl();
    }
}
