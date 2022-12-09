package com.github.linyuzai.thing.core.context;

public class ThingContextFactoryImpl implements ThingContextFactory {

    @Override
    public ThingContext create() {
        return new ThingContextImpl();
    }
}
