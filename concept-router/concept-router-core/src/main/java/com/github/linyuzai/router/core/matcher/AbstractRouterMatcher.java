package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractRouterMatcher<S extends Router.Source, R extends Router> implements RouterMatcher {

    @SuppressWarnings("unchecked")
    @Override
    public Router match(Router.Source source, Collection<? extends Router> routers) {
        return doMatch((S) source, routers.stream().map(it -> (R) it).collect(Collectors.toList()));
    }

    public abstract Router doMatch(S request, Collection<? extends R> routers);
}
