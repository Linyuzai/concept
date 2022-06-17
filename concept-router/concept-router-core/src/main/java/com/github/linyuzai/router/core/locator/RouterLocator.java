package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;

public interface RouterLocator {

    Router.Location locate(Router router, Collection<? extends Router.Location> locations);
}
