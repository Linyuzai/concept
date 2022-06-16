package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.Route;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface RouteRepository {

    default void add(Route... routes) {
        add(Arrays.asList(routes));
    }

    void add(Collection<? extends Route> routes);

    default void update(Route... routes) {
        update(Arrays.asList(routes));
    }

    void update(Collection<? extends Route> routes);

    default void remove(String... ids) {
        remove(Arrays.asList(ids));
    }

    void remove(Collection<? extends String> ids);

    Route get(String id);

    default List<Route> list(String... ids) {
        return list(Arrays.asList(ids));
    }

    List<Route> list(Collection<? extends String> ids);

    Collection<Route> all();
}
