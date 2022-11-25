package com.github.linyuzai.thing.core.concept;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface Container<T> {

    T get(String id);

    Optional<T> optional(String id);

    List<T> list();

    Stream<T> stream();
}
