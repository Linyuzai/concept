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
import com.github.linyuzai.plugin.core.metadata.EmptyMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFinder;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 插件概念抽象类
 */
@Getter
@Setter
public abstract class AbstractPluginConcept implements PluginConcept {

    protected PluginContextFactory contextFactory;

    protected PluginTreeFactory treeFactory;

    protected PluginHandlerChainFactory handlerChainFactory;

    protected PluginRepository repository;

    protected PluginEventPublisher eventPublisher;

    protected PluginLogger logger;

    protected Collection<PluginMetadataFinder> metadataFinders;

    protected Collection<PluginFactory> factories;

    protected Collection<PluginHandler> handlers;

    protected Collection<PluginHandlerFactory> handlerFactories;

    @Deprecated
    protected List<Runnable> posts = new CopyOnWriteArrayList<>();

    /**
     * 处理链
     */
    protected volatile PluginHandlerChain handlerChain;

    /**
     * 正在加载的插件
     */
    protected volatile Set<Object> loading = new LinkedHashSet<>();

    /**
     * 正在卸载的插件
     */
    protected volatile Set<Object> unloading = new LinkedHashSet<>();

    @Override
    public void initialize() {
        for (Runnable post : this.posts) {
            post.run();
        }
        eventPublisher.publish(new PluginConceptInitializedEvent(this));
    }

    @Override
    public void destroy() {
        //卸载所有插件
        repository.stream().forEach(this::unload);
        eventPublisher.publish(new PluginConceptDestroyedEvent(this));
    }

    @Deprecated
    @Override
    public void post(Runnable runnable) {
        this.posts.add(runnable);
    }

    @Override
    public void addHandlers(PluginHandler... handlers) {
        addHandlers(Arrays.asList(handlers));
    }

    @Override
    public void addHandlers(Collection<? extends PluginHandler> handlers) {
        this.handlers.addAll(handlers);
        //重置处理链
        resetHandlerChain();
    }

    @Override
    public void removeHandlers(PluginHandler... handlers) {
        removeHandlers(Arrays.asList(handlers));
    }

    @Override
    public void removeHandlers(Collection<? extends PluginHandler> handlers) {
        this.handlers.removeAll(handlers);
        //重置处理链
        resetHandlerChain();
    }

    /**
     * 获得处理链
     * <p>
     * 如果存在动态处理器则创建新处理链
     * <p>
     * 如果不存在动态处理器则使用缓存的处理链
     * <p>
     * 如果没有缓存则创建新处理链并缓存
     */
    protected PluginHandlerChain obtainHandlerChain(Plugin plugin, PluginContext context) {
        List<PluginHandler> dynamicHandlers = new ArrayList<>();
        //获得动态处理器
        for (PluginHandlerFactory factory : handlerFactories) {
            PluginHandler handler = factory.create(plugin, context, this);
            if (handler != null) {
                dynamicHandlers.add(handler);
            }
        }
        //有动态处理器需要重新生成处理链
        if (!dynamicHandlers.isEmpty()) {
            List<PluginHandler> combineHandlers = new ArrayList<>();
            combineHandlers.addAll(this.handlers);
            combineHandlers.addAll(dynamicHandlers);
            return handlerChainFactory.create(combineHandlers, context, this);
        }
        //获得缓存的处理链
        if (handlerChain == null) {
            synchronized (this) {
                if (handlerChain == null) {
                    handlerChain = handlerChainFactory.create(handlers, context, this);
                }
            }
        }
        return handlerChain;
    }

    /**
     * 重置处理链
     * <p>
     * 如果添加或移除了处理器就需要重新生成处理链
     */
    protected void resetHandlerChain() {
        handlerChain = null;
    }

    @Override
    public PluginMetadata metadata(Object source, PluginContext context) {
        if (source instanceof Plugin) {
            return ((Plugin) source).getMetadata();
        }
        for (PluginMetadataFinder finder : metadataFinders) {
            PluginMetadata metadata = finder.find(source, context);
            if (metadata != null) {
                return metadata;
            }
        }
        return null;
    }

    /**
     * 遍历插件工厂创建插件
     */
    @Override
    public Plugin create(Object source, PluginContext context) {
        if (source instanceof Plugin) {
            return (Plugin) source;
        }
        PluginMetadata metadata = metadata(source, context);
        if (metadata == null) {
            return null;
        }
        for (PluginFactory factory : factories) {
            Plugin plugin = factory.create(source, metadata, context);
            if (plugin != null) {
                plugin.setSource(source);
                plugin.setMetadata(metadata);
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
     * 加载插件
     * <p>
     * 加载已存在的插件将会失败
     * <p>
     * 插件创建后根据插件依赖进行解析提取
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
            loading.remove(source);//移除正在加载的状态
            repository.add(plugin);//添加到存储中
            onSuccess.accept(source, plugin);
        };

        BiConsumer<Object, Throwable> error = (source, e) -> {
            loading.remove(source);//移除正在加载的状态
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
                //创建插件
                Plugin plugin = create(source, context);
                if (plugin == null) {
                    throw new PluginException("Plugin can not create: " + source);
                }
                //初始化插件
                plugin.setConcept(this);
                plugin.initialize();

                context.set(Plugin.class, plugin);
                eventPublisher.publish(new PluginCreatedEvent(plugin));

                entries.add(new LoadingEntry(source, plugin, context));
            } catch (Throwable e) {
                error.accept(source, e);
            }
        }

        //遍历所有创建成功的插件进行解析，同时处理依赖关系
        while (!entries.isEmpty()) {
            LoadingEntry entry = entries.remove(0);
            loadDependency(entry, entries, new Stack<>(), success, error);
        }
    }

    /**
     * 基于依赖关系加载插件
     */
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
            //添加到依赖链
            dependencyChain.push(name);
        }
        try {
            //获得所有依赖的插件
            Set<String> dependencyNames = entry.getPlugin().getMetadata()
                    .asStandard().getDependency().getNames();
            //存在依赖的插件
            if (dependencyNames != null && !dependencyNames.isEmpty()) {
                for (String dependencyName : dependencyNames) {
                    if (existDependency(dependencyName)) {
                        //插件已经加载
                        continue;
                    }
                    //依赖链中已经存在判断为循环依赖
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
                    //获得匹配的插件
                    List<LoadingEntry> dependencyList = original.stream()
                            .filter(it -> Objects.equals(dependencyName, it.getPlugin()
                                    .getMetadata().asStandard().getName()))
                            .collect(Collectors.toList());
                    for (LoadingEntry dependency : dependencyList) {
                        //插件提前加载，从待加载的插件中移除
                        original.remove(dependency);
                        //加载依赖的插件
                        loadDependency(dependency, original, dependencyChain, onSuccess, onError);
                    }
                    //依赖的插件不存在抛出异常
                    if (!existDependency(dependencyName)) {
                        throw new PluginException("Plugin dependency not found: " + dependencyName);
                    }
                }
            }
            //创建插件树
            PluginTree tree = treeFactory.create(plugin, context, this);
            context.set(PluginTree.class, tree);
            context.set(PluginTree.Node.class, tree.getRoot());
            //准备插件
            plugin.prepare(context);
            eventPublisher.publish(new PluginPreparedEvent(context));
            //获取插件解析配置
            boolean handlerEnabled = plugin.getMetadata().asStandard().getHandler().isEnabled();
            if (handlerEnabled) {
                //解析插件
                PluginHandlerChain chain = obtainHandlerChain(plugin, context);
                plugin.load(chain, context);
            }
            //销毁上下文
            context.destroy();
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

    /**
     * 依赖是否存在
     */
    protected boolean existDependency(String name) {
        return repository.stream().anyMatch(it ->
                Objects.equals(name, it.getMetadata().asStandard().getName()));
    }

    /**
     * 卸载插件。
     * 通过插件的 id，source 或插件本身移除对应的插件
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
         */
        public B contextFactory(PluginContextFactory contextFactory) {
            this.contextFactory = contextFactory;
            return (B) this;
        }

        /**
         * 设置插件树工厂
         */
        public B treeFactory(PluginTreeFactory treeFactory) {
            this.treeFactory = treeFactory;
            return (B) this;
        }

        /**
         * 设置插件处理链工厂
         */
        public B handlerChainFactory(PluginHandlerChainFactory handlerChainFactory) {
            this.handlerChainFactory = handlerChainFactory;
            return (B) this;
        }

        /**
         * 设置插件仓储
         */
        public B repository(PluginRepository repository) {
            this.repository = repository;
            return (B) this;
        }

        /**
         * 设置事件发布者
         */
        public B eventPublisher(PluginEventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
            return (B) this;
        }

        /**
         * 设置日志
         */
        public B logger(PluginLogger logger) {
            this.logger = logger;
            return (B) this;
        }

        /**
         * 添加事件监听器
         */
        public B addEventListeners(PluginEventListener... listeners) {
            return addEventListeners(Arrays.asList(listeners));
        }

        /**
         * 添加事件监听器
         */
        public B addEventListeners(Collection<? extends PluginEventListener> listeners) {
            this.eventListeners.addAll(listeners);
            return (B) this;
        }

        /**
         * 添加插件工厂
         */
        public B addFactories(PluginFactory... factories) {
            return addFactories(Arrays.asList(factories));
        }

        /**
         * 添加插件工厂
         */
        public B addFactories(Collection<? extends PluginFactory> factories) {
            this.factories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加插件解析器
         */
        public B addResolvers(PluginResolver... resolvers) {
            return addHandlers(resolvers);
        }

        /**
         * 添加插件解析器
         */
        public B addResolvers(Collection<? extends PluginResolver> resolvers) {
            return addHandlers(resolvers);
        }

        /**
         * 添加插件过滤器
         */
        public B addFilters(PluginFilter... filters) {
            return addHandlers(filters);
        }

        /**
         * 添加插件过滤器
         */
        public B addFilters(Collection<? extends PluginFilter> filters) {
            return addHandlers(filters);
        }

        /**
         * 添加插件提取器
         */
        public B addExtractors(PluginExtractor... extractors) {
            return addHandlers(extractors);
        }

        /**
         * 添加插件提取器
         */
        public B addExtractors(Collection<? extends PluginExtractor> extractors) {
            return addHandlers(extractors);
        }

        /**
         * 添加插件处理器
         */
        public B addHandlers(PluginHandler... handlers) {
            return addHandlers(Arrays.asList(handlers));
        }

        /**
         * 添加插件处理器
         */
        public B addHandlers(Collection<? extends PluginHandler> handlers) {
            this.handlers.addAll(handlers);
            return (B) this;
        }

        /**
         * 添加插件处理器工厂
         */
        public B addHandlerFactories(PluginHandlerFactory... factories) {
            return addHandlerFactories(Arrays.asList(factories));
        }

        /**
         * 添加插件处理器工厂
         */
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
