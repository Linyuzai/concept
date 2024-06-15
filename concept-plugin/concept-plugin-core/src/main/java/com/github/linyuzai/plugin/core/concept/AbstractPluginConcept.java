package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.*;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.exception.PluginLoadException;
import com.github.linyuzai.plugin.core.exception.PluginUnloadException;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChain;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.lock.PluginLock;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;

/**
 * {@link PluginConcept} 抽象类
 */
@Getter
@Setter
public abstract class AbstractPluginConcept implements PluginConcept {

    /**
     * 上下文工厂
     */
    protected PluginContextFactory contextFactory;

    protected PluginTreeFactory treeFactory;

    protected PluginHandlerChainFactory handlerChainFactory;

    protected PluginRepository repository;

    protected PluginLock lock;

    /**
     * 事件发布者
     */
    protected PluginEventPublisher eventPublisher;

    protected PluginLogger logger;

    protected Collection<PluginHandler> handlers;

    /**
     * 插件工厂
     */
    protected Collection<PluginFactory> factories;

    /**
     * 插件提取器
     */
    protected Collection<PluginExtractor> extractors;

    protected volatile PluginHandlerChain handlerChain;

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {
        repository.stream().forEach(this::unload);
    }

    @Override
    public void addExtractors(PluginExtractor... extractors) {
        addExtractors(Arrays.asList(extractors));
    }

    @Override
    public void addExtractors(Collection<? extends PluginExtractor> extractors) {
        this.extractors.addAll(extractors);
        resetHandlerChain();
    }

    @Override
    public void removeExtractors(PluginExtractor... extractors) {
        removeExtractors(Arrays.asList(extractors));
    }

    @Override
    public void removeExtractors(Collection<? extends PluginExtractor> extractors) {
        this.extractors.removeAll(extractors);
        resetHandlerChain();
    }

    protected PluginHandlerChain obtainHandlerChain() {
        if (handlerChain == null) {
            synchronized (this) {
                if (handlerChain == null) {
                    //TODO 根据 提取器筛选解析器
                    handlerChain = handlerChainFactory.create(handlers);
                }
            }
        }
        return handlerChain;
    }

    protected void resetHandlerChain() {
        handlerChain = null;
    }

    /**
     * 创建插件，如创建失败则抛出异常
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    @Override
    public Plugin create(Object o, PluginContext context) {
        if (o instanceof Plugin) {
            return (Plugin) o;
        }
        for (PluginFactory factory : factories) {
            Plugin plugin = factory.create(o, context);
            if (plugin != null) {
                return plugin;
            }
        }
        return null;
    }

    /**
     * 加载插件。
     * 通过 {@link PluginFactory} 创建插件，
     * 准备插件 {@link Plugin#prepare(PluginContext)}，
     * 通过 {@link PluginContextFactory} 创建上下文 {@link PluginContext} 并初始化，
     * 执行插件解析链 {@link PluginResolver}，
     * 通过 {@link PluginExtractor} 提取插件，
     * 销毁上下文，释放插件资源。
     *
     * @param o 插件源
     */
    @Override
    public Plugin load(@NonNull Object o) {
        lock.lock(o, PluginLock.LOADING);
        if (repository.contains(o)) {
            throw new IllegalArgumentException("Plugin is already loaded: " + o);
        }
        //创建上下文
        PluginContext context = contextFactory.create(this);
        try {
            //初始化上下文
            context.set(PluginConcept.class, this);
            context.initialize();

            Plugin plugin = create(o, context);
            if (plugin == null) {
                throw new PluginException("Plugin can not create: " + o);
            }
            plugin.setConcept(this);
            plugin.initialize();
            eventPublisher.publish(new PluginCreatedEvent(plugin));

            PluginTree tree = treeFactory.create(plugin, this);
            context.set(Plugin.class, plugin);
            context.set(PluginTree.class, tree);
            context.set(PluginTree.Node.class, tree.getRoot());

            //准备插件
            plugin.prepare(context);
            eventPublisher.publish(new PluginPreparedEvent(context));

            //解析插件
            obtainHandlerChain().next(context);
            //提取插件
            for (PluginExtractor extractor : extractors) {
                extractor.extract(context);
            }

            plugin.release(context);
            eventPublisher.publish(new PluginReleasedEvent(context));

            //销毁上下文
            context.destroy();
            repository.add(o, plugin);
            eventPublisher.publish(new PluginLoadedEvent(plugin));

            return plugin;
        } catch (Throwable e) {
            throw new PluginLoadException(context, e);
        } finally {
            lock.unlock(o, PluginLock.LOADING);
        }
    }

    /**
     * 卸载插件。
     * 通过插件的 id 或插件本身移除对应的插件
     *
     * @param o 插件源
     */
    @Override
    public Plugin unload(@NonNull Object o) {
        lock.lock(o, PluginLock.UNLOADING);
        try {
            Plugin removed = repository.remove(o);
            if (removed != null) {
                removed.destroy();
                eventPublisher.publish(new PluginUnloadedEvent(removed));
            }
            return removed;
        } catch (Throwable e) {
            throw new PluginUnloadException(o, e);
        } finally {
            lock.unlock(o, PluginLock.UNLOADING);
        }
    }

    @Override
    public boolean isLoaded(Object o) {
        return repository.contains(o);
    }

    @Override
    public boolean isLoading(Object o) {
        return PluginLock.LOADING.equals(lock.getLockArg(o));
    }

    @Override
    public boolean isUnloading(Object o) {
        return PluginLock.UNLOADING.equals(lock.getLockArg(o));
    }

    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<B extends AbstractBuilder<B, T>, T extends AbstractPluginConcept> {

        protected PluginContextFactory contextFactory;

        protected PluginTreeFactory treeFactory;

        protected PluginHandlerChainFactory handlerChainFactory;

        protected PluginRepository repository;

        protected PluginLock lock;

        protected PluginEventPublisher eventPublisher;

        protected PluginLogger logger;

        protected List<PluginEventListener> eventListeners = new ArrayList<>();

        protected List<PluginFactory> factories = new ArrayList<>();

        protected List<PluginHandler> handlers = new ArrayList<>();

        protected List<PluginExtractor> extractors = new ArrayList<>();

        /**
         * 设置上下文工厂
         *
         * @param contextFactory 上下文工厂
         * @return {@link B}
         */
        public B contextFactory(PluginContextFactory contextFactory) {
            this.contextFactory = contextFactory;
            return (B) this;
        }

        public B treeFactory(PluginTreeFactory treeFactory) {
            this.treeFactory = treeFactory;
            return (B) this;
        }

        public B handlerChainFactory(PluginHandlerChainFactory handlerChainFactory) {
            this.handlerChainFactory = handlerChainFactory;
            return (B) this;
        }

        public B repository(PluginRepository repository) {
            this.repository = repository;
            return (B) this;
        }

        public B lock(PluginLock lock) {
            this.lock = lock;
            return (B) this;
        }

        /**
         * 设置事件发布者
         *
         * @param eventPublisher 事件发布者
         * @return {@link B}
         */
        public B eventPublisher(PluginEventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
            return (B) this;
        }

        public B logger(PluginLogger logger) {
            this.logger = logger;
            return (B) this;
        }

        /**
         * 添加事件监听器
         *
         * @param listeners 事件监听器
         * @return {@link B}
         */
        public B addEventListeners(PluginEventListener... listeners) {
            return addEventListeners(Arrays.asList(listeners));
        }

        /**
         * 添加事件监听器
         *
         * @param listeners 事件监听器
         * @return {@link B}
         */
        public B addEventListeners(Collection<? extends PluginEventListener> listeners) {
            this.eventListeners.addAll(listeners);
            return (B) this;
        }

        /**
         * 添加插件工厂
         *
         * @param factories 插件工厂
         * @return {@link B}
         */
        public B addFactories(PluginFactory... factories) {
            return addFactories(Arrays.asList(factories));
        }

        /**
         * 添加插件工厂
         *
         * @param factories 插件工厂
         * @return {@link B}
         */
        public B addFactories(Collection<? extends PluginFactory> factories) {
            this.factories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加插件解析器
         *
         * @param resolvers 插件解析器
         * @return {@link B}
         */
        public B addResolvers(PluginResolver... resolvers) {
            return addResolvers(Arrays.asList(resolvers));
        }

        /**
         * 添加插件解析器
         *
         * @param resolvers 插件解析器
         * @return {@link B}
         */
        public B addResolvers(Collection<? extends PluginResolver> resolvers) {
            this.handlers.addAll(resolvers);
            return (B) this;
        }

        /**
         * 添加插件过滤器
         *
         * @param filters 插件过滤器
         * @return {@link B}
         */
        public B addFilters(PluginFilter... filters) {
            return addFilters(Arrays.asList(filters));
        }

        /**
         * 添加插件过滤器
         *
         * @param filters 插件过滤器
         * @return {@link B}
         */
        public B addFilters(Collection<? extends PluginFilter> filters) {
            this.handlers.addAll(filters);
            return (B) this;
        }

        /**
         * 添加插件提取器
         *
         * @param extractors 插件提取器
         * @return {@link B}
         */
        public B addExtractors(PluginExtractor... extractors) {
            return addExtractors(Arrays.asList(extractors));
        }

        /**
         * 添加插件提取器
         *
         * @param extractors 插件提取器
         * @return {@link B}
         */
        public B addExtractors(Collection<? extends PluginExtractor> extractors) {
            this.extractors.addAll(extractors);
            return (B) this;
        }

        public T build() {
            T concept = create();
            eventPublisher.register(eventListeners);
            concept.setContextFactory(contextFactory);
            concept.setTreeFactory(treeFactory);
            concept.setHandlerChainFactory(handlerChainFactory);
            concept.setRepository(repository);
            concept.setLock(lock);
            concept.setEventPublisher(eventPublisher);
            concept.setLogger(logger);
            concept.setFactories(factories);
            concept.setHandlers(handlers);
            concept.setExtractors(extractors);
            return concept;
        }

        protected abstract T create();
    }
}
