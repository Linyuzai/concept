package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.*;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.exception.PluginLoadException;
import com.github.linyuzai.plugin.core.exception.PluginUnloadException;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChain;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandlerFactory;
import com.github.linyuzai.plugin.core.handle.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;
import com.github.linyuzai.plugin.core.handle.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.lock.PluginLock;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

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

    /**
     * 插件工厂
     */
    protected Collection<PluginFactory> factories;

    protected Collection<PluginHandler> handlers;

    protected Collection<PluginHandlerFactory> handlerFactories;

    protected volatile PluginHandlerChain handlerChain;

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {
        repository.stream().forEach(this::unload);
    }

    @Override
    public void addHandlers(PluginHandler... handlers) {
        addHandlers(Arrays.asList(handlers));
    }

    @Override
    public void addHandlers(Collection<? extends PluginHandler> handlers) {
        this.handlers.addAll(handlers);
        resetHandlerChain();
    }

    @Override
    public void removeHandlers(PluginHandler... handlers) {
        removeHandlers(Arrays.asList(handlers));
    }

    @Override
    public void removeHandlers(Collection<? extends PluginHandler> handlers) {
        this.handlers.removeAll(handlers);
        resetHandlerChain();
    }

    protected PluginHandlerChain obtainHandlerChain(PluginContext context) {
        List<PluginHandler> dynamicHandlers = new ArrayList<>();
        for (PluginHandlerFactory factory : handlerFactories) {
            PluginHandler handler = factory.create(context);
            if (handler != null) {
                dynamicHandlers.add(handler);
            }
        }
        if (!dynamicHandlers.isEmpty()) {
            List<PluginHandler> combineHandlers = new ArrayList<>();
            combineHandlers.addAll(this.handlers);
            combineHandlers.addAll(dynamicHandlers);
            return handlerChainFactory.create(combineHandlers);
        }
        if (handlerChain == null) {
            synchronized (this) {
                if (handlerChain == null) {
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

    @Override
    public Plugin load(Object o) {
        return load(o, false);
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
    public Plugin load(@NonNull Object o, boolean reloadIfExist) {
        Plugin exist = repository.get(o);
        if (exist != null && !reloadIfExist) {
            throw new IllegalArgumentException("Plugin is already loaded: " + o);
        }
        //创建上下文
        PluginContext context = contextFactory.create(this);

        return load(o, context, plugin -> {
            repository.add(plugin);
            if (exist != null) {
                try {
                    exist.destroy();
                } catch (Throwable e) {
                    logger.error("Plugin destroy", e);
                }
            }
        }, e -> {
            if (e instanceof PluginLoadException) {
                throw (PluginLoadException) e;
            } else {
                throw new PluginLoadException(context, e);
            }
        }, () -> {
        });
    }

    @Override
    public Plugin load(Object o, PluginContext context, Consumer<Plugin> onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        lock.lock(o, PluginLock.LOADING);
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

            Boolean handlerEnabled = plugin.getMetadata()
                    .property(PluginHandler.PropertyKeys.ENABLED, Boolean.TRUE.toString());

            if (handlerEnabled) {
                //解析插件
                obtainHandlerChain(context).next(context);
            }

            plugin.release(context);
            eventPublisher.publish(new PluginReleasedEvent(context));

            //销毁上下文
            context.destroy();

            onSuccess.accept(plugin);

            eventPublisher.publish(new PluginLoadedEvent(plugin));

            return plugin;
        } catch (Throwable e) {
            onError.accept(e);
            return null;
        } finally {
            onComplete.run();
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

        protected List<PluginHandlerFactory> handlerFactories = new ArrayList<>();

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
            return addHandlers(resolvers);
        }

        /**
         * 添加插件解析器
         *
         * @param resolvers 插件解析器
         * @return {@link B}
         */
        public B addResolvers(Collection<? extends PluginResolver> resolvers) {
            return addHandlers(resolvers);
        }

        /**
         * 添加插件过滤器
         *
         * @param filters 插件过滤器
         * @return {@link B}
         */
        public B addFilters(PluginFilter... filters) {
            return addHandlers(filters);
        }

        /**
         * 添加插件过滤器
         *
         * @param filters 插件过滤器
         * @return {@link B}
         */
        public B addFilters(Collection<? extends PluginFilter> filters) {
            return addHandlers(filters);
        }

        /**
         * 添加插件提取器
         *
         * @param extractors 插件提取器
         * @return {@link B}
         */
        public B addExtractors(PluginExtractor... extractors) {
            return addHandlers(extractors);
        }

        /**
         * 添加插件提取器
         *
         * @param extractors 插件提取器
         * @return {@link B}
         */
        public B addExtractors(Collection<? extends PluginExtractor> extractors) {
            return addHandlers(extractors);
        }

        public B addHandlers(PluginHandler... handlers) {
            return addHandlers(Arrays.asList(handlers));
        }

        public B addHandlers(Collection<? extends PluginHandler> handlers) {
            this.handlers.addAll(handlers);
            return (B) this;
        }

        public B addHandlerFactories(PluginHandlerFactory... factories) {
            return addHandlerFactories(Arrays.asList(factories));
        }

        public B addHandlerFactories(Collection<? extends PluginHandlerFactory> factories) {
            this.handlerFactories.addAll(factories);
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
            concept.setHandlerFactories(handlerFactories);
            return concept;
        }

        protected abstract T create();
    }
}
