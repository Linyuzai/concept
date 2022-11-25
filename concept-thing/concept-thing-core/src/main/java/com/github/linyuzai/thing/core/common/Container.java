package com.github.linyuzai.thing.core.common;

import com.github.linyuzai.thing.core.operation.Operation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface Container<T extends Containable> {

    Operation add(T one);

    Operation remove(String id);

    T find(String id);

    Optional<T> optional(String id);

    List<T> list();

    Stream<T> stream();
}
