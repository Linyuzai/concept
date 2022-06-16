package com.github.linyuzai.router.core.concept;


import java.util.Collection;

public interface RouterConcept {

    Route.Location route(Route.Source source, Collection<? extends Route.Location> locations);
}
