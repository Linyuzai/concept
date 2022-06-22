package com.github.linyuzai.router.core.concept;

import com.github.linyuzai.router.core.event.DefaultRouterEventPublisher;
import com.github.linyuzai.router.core.event.RouterEventPublisher;
import com.github.linyuzai.router.core.event.RouterLocateEvent;
import com.github.linyuzai.router.core.event.RouterMatchEvent;
import com.github.linyuzai.router.core.exception.RouterException;
import com.github.linyuzai.router.core.locator.RouterLocator;
import com.github.linyuzai.router.core.matcher.RouterMatcher;
import com.github.linyuzai.router.core.repository.InMemoryRouterRepository;
import com.github.linyuzai.router.core.repository.RouterRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultRouterConcept implements RouterConcept {

    private RouterRepository repository;

    private RouterMatcher matcher;

    private RouterLocator locator;

    private RouterEventPublisher eventPublisher;

    @Override
    public Router.Location route(Router.Source source, Collection<? extends Router.Location> locations) {
        Router router = matcher.match(source, repository.all());
        publish(new RouterMatchEvent(source, router));
        if (router == null) {
            return Router.Location.UNMATCHED;
        }
        Router.Location location = locator.locate(router, locations);
        publish(new RouterLocateEvent(location, router));
        return location;
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

    public static final class Builder {

        private RouterRepository repository;

        private RouterMatcher matcher;

        private RouterLocator locator;

        private RouterEventPublisher eventPublisher;

        public Builder repository(RouterRepository repository) {
            this.repository = repository;
            return this;
        }

        public Builder matcher(RouterMatcher matcher) {
            this.matcher = matcher;
            return this;
        }

        public Builder locator(RouterLocator locator) {
            this.locator = locator;
            return this;
        }

        public Builder eventPublisher(RouterEventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
            return this;
        }

        public DefaultRouterConcept build() {
            if (matcher == null) {
                throw new RouterException("RouterMatcher is null");
            }
            if (locator == null) {
                throw new RouterException("RouterLocator is null");
            }
            if (repository == null) {
                repository = new InMemoryRouterRepository();
            }
            if (eventPublisher == null) {
                eventPublisher = new DefaultRouterEventPublisher();
            }
            return new DefaultRouterConcept(repository, matcher, locator, eventPublisher);
        }
    }
}
