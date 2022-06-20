package com.github.linyuzai.router.core.concept;

import java.util.Collection;
import java.util.List;

public interface RouterConcept {

    Router.Location route(Router.Source source, Collection<? extends Router.Location> locations);

    List<Router> routers();

    void add(Router router);

    void update(Router router);

    void delete(String id);

    void publish(Object event);
}
