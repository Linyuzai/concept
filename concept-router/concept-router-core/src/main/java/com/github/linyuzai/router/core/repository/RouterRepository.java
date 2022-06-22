package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface RouterRepository {

    default void initialize(){

    }

    default void destroy() {

    }

    default void add(Router... routers) {
        add(Arrays.asList(routers));
    }

    void add(Collection<? extends Router> routes);

    default void update(Router... routers) {
        update(Arrays.asList(routers));
    }

    void update(Collection<? extends Router> routes);

    default void remove(String... ids) {
        remove(Arrays.asList(ids));
    }

    void remove(Collection<? extends String> ids);

    Router get(String id);

    default List<Router> list(String... ids) {
        return list(Arrays.asList(ids));
    }

    List<Router> list(Collection<? extends String> ids);

    List<Router> all();
}
