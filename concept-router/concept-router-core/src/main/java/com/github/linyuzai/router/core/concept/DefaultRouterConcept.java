package com.github.linyuzai.router.core.concept;

import com.github.linyuzai.router.core.event.RouterEventPublisher;
import com.github.linyuzai.router.core.event.RouterMatchedEvent;
import com.github.linyuzai.router.core.locator.RouterLocator;
import com.github.linyuzai.router.core.matcher.RouterMatcher;
import com.github.linyuzai.router.core.repository.RouterRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class DefaultRouterConcept implements RouterConcept {

    private RouterRepository repository;

    private RouterMatcher matcher;

    private RouterLocator locator;

    private RouterEventPublisher eventPublisher;

    @Override
    public Router.Location route(Router.Source source, Collection<? extends Router.Location> locations) {
        Router router = matcher.match(source, repository.all());
        if (router == null) {
            return null;
        }
        publish(new RouterMatchedEvent(router));
        return locator.locate(router, locations);
    }

    @Override
    public List<Router> routers() {
        return repository.all();
    }

    @Override
    public void add(Router router) {
        repository.add(router);
    }

    @Override
    public void update(Router router) {
        repository.update(router);
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    }

    @Override
    public void publish(Object event) {
        eventPublisher.publish(event);
    }
}
