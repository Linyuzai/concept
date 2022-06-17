package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;

public interface RouterMatcher {

    Router match(Router.Source source, Collection<? extends Router> routes);
}
