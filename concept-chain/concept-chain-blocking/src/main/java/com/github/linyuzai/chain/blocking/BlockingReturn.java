package com.github.linyuzai.chain.blocking;

import com.github.linyuzai.chain.core.Return;

public class BlockingReturn<T> implements Return<T> {

    private Object value;

    @Override
    public Return<T> empty() {
        return this;
    }

    @Override
    public Return<T> set(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public Return<T> wrap(T value) {
        this.value = value;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R unwrap() {
        return (R) value;
    }
}
