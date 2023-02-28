package com.github.linyuzai.chain.core;

import java.util.function.Function;

public interface Return<T> {

    Return<T> empty();

    Return<T> set(Object value);

    <R> Return<R> map(Function<T, R> function);

    T get();

    Return<T> wrap(T value);

    <R> R unwrap();
}
