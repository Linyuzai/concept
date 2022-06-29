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

/**
 * 默认的 {@link RouterConcept} 实现
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultRouterConcept implements RouterConcept {

    /**
     * 路由仓库
     */
    private RouterRepository repository;

    /**
     * 路由匹配器
     */
    private RouterMatcher matcher;

    /**
     * 路由定位器
     */
    private RouterLocator locator;

    /**
     * 路由事件发布器
     */
    private RouterEventPublisher eventPublisher;

    @Override
    public Router.Location route(Router.Source source, Collection<? extends Router.Location> locations) {
        //路由匹配器通过路由来源在所有的路由中进行匹配
        Router router = matcher.match(source, repository.all());
        //发布路由匹配事件，路由可能为null
        publish(new RouterMatchEvent(source, router));
        //如果路由为null则直接返回未匹配
        if (router == null) {
            return Router.Location.UNMATCHED;
        }
        //路由定位器通过匹配到的路由在所有的位置中进行定位
        Router.Location location = locator.locate(router, locations);
        //发布路由定位事件，位置可能为不可用
        publish(new RouterLocateEvent(location, router));
        //如果为null则返回不可用
        //如果自定义定位器返回null则会导致异常
        if (location == null) {
            return Router.Location.UNAVAILABLE;
        }
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

        /**
         * 配置路由仓库
         *
         * @param repository 路由仓库
         * @return {@link Builder} 本身
         */
        public Builder repository(RouterRepository repository) {
            this.repository = repository;
            return this;
        }

        /**
         * 配置路由匹配器
         *
         * @param matcher 路由匹配器
         * @return {@link Builder} 本身
         */
        public Builder matcher(RouterMatcher matcher) {
            this.matcher = matcher;
            return this;
        }

        /**
         * 配置路由定位器
         *
         * @param locator 路由定位器
         * @return {@link Builder} 本身
         */
        public Builder locator(RouterLocator locator) {
            this.locator = locator;
            return this;
        }

        /**
         * 配置路由事件发布器
         *
         * @param eventPublisher 路由事件发布器
         * @return {@link Builder} 本身
         */
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
