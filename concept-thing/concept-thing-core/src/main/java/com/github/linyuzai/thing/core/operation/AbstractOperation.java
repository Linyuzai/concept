package com.github.linyuzai.thing.core.operation;

public abstract class AbstractOperation implements Operation {

    @Override
    public <T> T convert(Class<T> target) {
        return null;
    }
}
