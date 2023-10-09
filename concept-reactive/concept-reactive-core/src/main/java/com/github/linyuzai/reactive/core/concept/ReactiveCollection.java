package com.github.linyuzai.reactive.core.concept;

public interface ReactiveCollection<T> {

    interface Factory {

        <T> ReactiveCollection<T> empty();
    }
}
