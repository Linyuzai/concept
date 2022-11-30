package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.IdAndKey;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface Container<T extends IdAndKey> {

    T get(String id);

    default Optional<T> optional(String id) {
        return Optional.ofNullable(get(id));
    }

    List<T> list();

    default Stream<T> stream() {
        return list().stream();
    }

    ThingAction add(T add);

    ThingAction remove(String id);
}
