package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.IdAndKey;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface Container<T extends IdAndKey> {

    T get(String id);

    Optional<T> optional(String id);

    List<T> list();

    Stream<T> stream();

    interface Modifiable<T extends IdAndKey> {

        ThingActionChain add(T one);

        ThingActionChain add(T one, AddInterceptor<T> interceptor);

        ThingActionChain remove(String id);

        ThingActionChain remove(String id, RemoveInterceptor<T> interceptor);
    }

    interface AddInterceptor<T extends IdAndKey> {

        default boolean beforeAdd(T add) {
            return true;
        }

        default void afterAdd(T add) {

        }
    }

    interface RemoveInterceptor<T extends IdAndKey> {

        default boolean beforeRemove(String id) {
            return true;
        }

        default void afterRemove(T remove) {

        }
    }
}
