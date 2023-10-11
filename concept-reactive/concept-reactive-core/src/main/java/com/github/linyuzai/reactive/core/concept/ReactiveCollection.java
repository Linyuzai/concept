package com.github.linyuzai.reactive.core.concept;

import java.util.List;
import java.util.function.Function;

public interface ReactiveCollection<T> extends ReactivePublisher<T> {

    <R> ReactiveCollection<R> flatMap(Function<? super T, ? extends ReactivePublisher<? extends R>> mapper);

    ReactiveObject<List<T>> collectList();

    interface Factory {

        <T> ReactiveCollection<T> empty();

        <T> ReactiveCollection<T> fromIterable(Iterable<? extends T> it);
    }
}
