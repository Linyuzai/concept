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
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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

    protected volatile Set<Object> loading = new LinkedHashSet<>();

    protected volatile Set<Object> unloading = new LinkedHashSet<>();

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
    public Plugin load(@NonNull Object source) {
        List<Plugin> plugins = new ArrayList<>();
        load(Collections.singleton(source), (o, plugin) -> plugins.add(plugin), (o, e) -> {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new PluginLoadException(source, e);
            }
        });
        return plugins.get(0);
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
     * @param sources 插件源
     */
    @Override
    public synchronized void load(@NonNull Collection<?> sources,
                                  @NonNull BiConsumer<Object, Plugin> onSuccess,
                                  @NonNull BiConsumer<Object, Throwable> onError) {
        List<Object> list = new ArrayList<>();
        for (Object source : sources) {
            if (repository.contains(source)) {
                onError.accept(source, new IllegalArgumentException("Plugin is already loaded: " + source));
            } else {
                loading.add(source);
                list.add(source);
            }
        }

        BiConsumer<Object, Plugin> success = (source, plugin) -> {
            loading.remove(source);
            onSuccess.accept(source, plugin);
        };

        BiConsumer<Object, Throwable> error = (source, e) -> {
            loading.remove(source);
            if (e instanceof PluginLoadException) {
                onError.accept(source, e);
            } else {
                onError.accept(source, new PluginLoadException(source, e));
            }
        };

        List<LoadingEntry> entries = new ArrayList<>();

        for (Object source : list) {
            try {
                //创建上下文
                PluginContext context = contextFactory.create(this);

                //初始化上下文
                context.set(PluginConcept.class, this);
                context.initialize();

                Plugin plugin = create(source, context);
                if (plugin == null) {
                    throw new PluginException("Plugin can not create: " + source);
                }

                plugin.setConcept(this);
                plugin.initialize();

                context.set(Plugin.class, plugin);

                eventPublisher.publish(new PluginCreatedEvent(plugin));

                entries.add(new LoadingEntry(source, plugin, context));
            } catch (Throwable e) {
                error.accept(source, e);
            }
        }

        while (!entries.isEmpty()) {
            LoadingEntry entry = entries.remove(0);
            loadDependency(entry, entries, new Stack<>(), success, error);
        }
    }

    protected void loadDependency(LoadingEntry entry,
                                  Collection<LoadingEntry> original,
                                  Stack<String> dependencyChain,
                                  BiConsumer<Object, Plugin> onSuccess,
                                  BiConsumer<Object, Throwable> onError) {
        Object source = entry.getSource();
        Plugin plugin = entry.getPlugin();
        PluginContext context = entry.getContext();
        String name = entry.getPlugin().getMetadata().asStandard().getName();
        if (name != null) {
            dependencyChain.push(name);
        }
        try {
            Set<String> dependencyNames = entry.getPlugin().getMetadata()
                    .asStandard().getDependency().getNames();
            if (dependencyNames != null && !dependencyNames.isEmpty()) {
                for (String dependencyName : dependencyNames) {
                    if (existDependency(dependencyName)) {
                        continue;
                    }

                    if (dependencyChain.contains(dependencyName)) {
                        for (int i = 0; i < dependencyChain.size(); i++) {
                            String n = dependencyChain.get(i);
                            if (Objects.equals(n, dependencyName)) {
                                List<String> subList = dependencyChain.subList(i, dependencyChain.size());
                                List<String> cycle = new ArrayList<>(subList);
                                cycle.add(dependencyName);
                                String message = "Plugin cycle dependency: " + String.join(" <== ", cycle);
                                throw new PluginException(message);
                            }
                        }
                    }

                    List<LoadingEntry> dependencyList = original.stream()
                            .filter(it -> Objects.equals(dependencyName, it.getPlugin()
                                    .getMetadata().asStandard().getName()))
                            .collect(Collectors.toList());
                    for (LoadingEntry dependency : dependencyList) {
                        original.remove(dependency);
                        loadDependency(dependency, original, dependencyChain, onSuccess, onError);
                    }

                    if (!existDependency(dependencyName)) {
                        throw new PluginException("Plugin dependency not found: " + dependencyName);
                    }
                }
            }

            PluginTree tree = treeFactory.create(plugin, this);
            context.set(PluginTree.class, tree);
            context.set(PluginTree.Node.class, tree.getRoot());

            //准备插件
            plugin.prepare(context);
            eventPublisher.publish(new PluginPreparedEvent(context));

            boolean handlerEnabled = plugin.getMetadata().asStandard().getHandler().isEnabled();

            if (handlerEnabled) {
                //解析插件
                plugin.load(obtainHandlerChain(context), context);
            }

            //销毁上下文
            context.destroy();

            repository.add(plugin);

            eventPublisher.publish(new PluginLoadedEvent(plugin));

            onSuccess.accept(source, plugin);

        } catch (Throwable e) {
            onError.accept(source, e);
        } finally {
            if (name != null) {
                dependencyChain.pop();
            }
        }
    }

    protected boolean existDependency(String name) {
        return repository.stream().anyMatch(it ->
                Objects.equals(name, it.getMetadata().asStandard().getName()));
    }

    /**
     * 卸载插件。
     * 通过插件的 id 或插件本身移除对应的插件
     *
     * @param source 插件源
     */
    @Override
    public synchronized Plugin unload(@NonNull Object source) {
        unloading.add(source);
        try {
            Plugin removed = repository.remove(source);
            if (removed != null) {
                removed.destroy();
                eventPublisher.publish(new PluginUnloadedEvent(removed));
            }
            return removed;
        } catch (Throwable e) {
            throw new PluginUnloadException(source, e);
        } finally {
            unloading.remove(source);
        }
    }

    @Override
    public boolean isLoading(Object source) {
        return loading.contains(source);
    }

    @Override
    public boolean isUnloading(Object source) {
        return unloading.contains(source);
    }

    @Override
    public boolean isLoaded(Object o) {
        return repository.contains(o);
    }

    @Getter
    @RequiredArgsConstructor
    public static class LoadingEntry {

        private final Object source;

        private final Plugin plugin;

        private final PluginContext context;
    }

    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<B extends AbstractBuilder<B, T>, T extends AbstractPluginConcept> {

        protected PluginContextFactory contextFactory;

        protected PluginTreeFactory treeFactory;

        protected PluginHandlerChainFactory handlerChainFactory;

        protected PluginRepository repository;

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
