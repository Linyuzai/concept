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
import com.github.linyuzai.plugin.core.intercept.PluginInterceptor;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import com.github.linyuzai.plugin.core.path.PluginPathFactory;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.sync.SyncSupport;
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
 * 插件概念抽象类
 */
@Getter
@Setter
public abstract class AbstractPluginConcept extends SyncSupport implements PluginConcept {

    protected PluginPathFactory pathFactory;

    protected PluginContextFactory contextFactory;

    protected PluginTreeFactory treeFactory;

    protected PluginHandlerChainFactory handlerChainFactory;

    protected PluginStorage storage;

    protected PluginRepository repository;

    protected PluginEventPublisher eventPublisher;

    protected PluginLogger logger;

    protected List<PluginMetadataFactory> metadataFactories;

    protected List<PluginFactory> factories;

    protected List<PluginHandler> handlers;

    protected List<PluginHandlerFactory> handlerFactories;

    protected List<PluginInterceptor> interceptors;

    /**
     * 处理链
     */
    protected PluginHandlerChain handlerChain;

    /**
     * 正在加载的插件
     */
    protected final Set<String> loading = new HashSet<>();

    /**
     * 正在卸载的插件
     */
    protected final Set<String> unloading = new HashSet<>();

    @Override
    public void initialize() {
        syncWrite(() -> eventPublisher.publish(new PluginConceptInitializedEvent(this)));
    }

    @Override
    public void destroy() {
        syncWrite(() -> {
            //卸载所有插件
            repository.stream().forEach(it -> unload(it.getDefinition()));
            eventPublisher.publish(new PluginConceptDestroyedEvent(this));
        });
    }

    @Override
    public void addHandlers(PluginHandler... handlers) {
        addHandlers(Arrays.asList(handlers));
    }

    @Override
    public void addHandlers(Collection<? extends PluginHandler> handlers) {
        syncWrite(() -> {
            this.handlers.addAll(handlers);
            //重置处理链
            resetHandlerChain();
        });
    }

    @Override
    public void removeHandlers(PluginHandler... handlers) {
        removeHandlers(Arrays.asList(handlers));
    }

    @Override
    public void removeHandlers(Collection<? extends PluginHandler> handlers) {
        syncWrite(() -> {
            this.handlers.removeAll(handlers);
            //重置处理链
            resetHandlerChain();
        });
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
            handlerChain = handlerChainFactory.create(handlers, context, this);
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
    public void addInterceptor(PluginInterceptor... interceptors) {
        addInterceptor(Arrays.asList(interceptors));
    }

    @Override
    public void addInterceptor(Collection<? extends PluginInterceptor> interceptors) {
        syncWrite(() -> this.interceptors.addAll(interceptors));
    }

    @Override
    public void removeInterceptor(PluginInterceptor... interceptors) {
        removeInterceptor(Arrays.asList(interceptors));
    }

    @Override
    public void removeInterceptor(Collection<? extends PluginInterceptor> interceptors) {
        syncWrite(() -> this.interceptors.removeAll(interceptors));
    }

    @Override
    public PluginContext createContext() {
        return contextFactory.create(this);
    }

    @Override
    public PluginMetadata createMetadata(PluginDefinition definition, PluginContext context) {
        return syncWrite(() -> {
            for (PluginMetadataFactory factory : metadataFactories) {
                PluginMetadata metadata = factory.create(definition, context);
                if (metadata != null) {
                    return metadata;
                }
            }
            return null;
        });
    }

    /**
     * 遍历插件工厂创建插件
     */
    @Override
    public Plugin createPlugin(PluginDefinition definition, PluginContext context) {
        return syncWrite(() -> {
            for (PluginInterceptor interceptor : interceptors) {
                interceptor.beforeCreatePlugin(definition, context);
            }
            for (PluginInterceptor interceptor : interceptors) {
                interceptor.beforeCreateMetadata(definition, context);
            }
            PluginMetadata metadata = createMetadata(definition, context);
            if (metadata == null) {
                return null;
            }
            for (PluginInterceptor interceptor : interceptors) {
                interceptor.afterCreateMetadata(metadata, definition, context);
            }
            for (PluginFactory factory : factories) {
                Plugin plugin = factory.create(definition, metadata, context);
                if (plugin != null) {
                    plugin.setDefinition(definition);
                    plugin.setMetadata(metadata);
                    for (PluginInterceptor interceptor : interceptors) {
                        interceptor.afterCreatePlugin(plugin, definition, context);
                    }
                    return plugin;
                }
            }
            return null;
        });
    }

    @Override
    public Plugin load(PluginDefinition definition) {
        List<Plugin> plugins = new ArrayList<>();
        load(Collections.singleton(definition),
                (d, plugin) -> plugins.add(plugin),
                (d, e) -> {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new PluginLoadException(definition, e);
                    }
                });
        if (plugins.isEmpty()) {
            return null;
        } else {
            return plugins.get(0);
        }
    }

    /**
     * 加载插件
     * <p>
     * 加载已存在的插件将会失败
     * <p>
     * 插件创建后根据插件依赖进行解析提取
     */
    @Override
    public void load(@NonNull Collection<? extends PluginDefinition> definitions,
                     @NonNull BiConsumer<PluginDefinition, Plugin> onSuccess,
                     @NonNull BiConsumer<PluginDefinition, Throwable> onError) {
        syncWrite(() -> {
            List<PluginDefinition> list = new ArrayList<>();
            for (PluginDefinition definition : definitions) {
                String path = definition.getPath();
                if (repository.contains(definition)) {
                    onError.accept(definition, new IllegalArgumentException("Plugin is already loaded: " + path));
                } else {
                    loading.add(path);
                    list.add(definition);
                }
            }

            BiConsumer<PluginDefinition, Plugin> success = (definition, plugin) -> {
                repository.add(plugin);//添加到存储中
                loading.remove(definition.getPath());//移除正在加载的状态
                onSuccess.accept(definition, plugin);
            };

            BiConsumer<PluginDefinition, Throwable> error = (definition, e) -> {
                loading.remove(definition.getPath());//移除正在加载的状态
                if (e instanceof PluginLoadException) {
                    onError.accept(definition, e);
                } else {
                    onError.accept(definition, new PluginLoadException(definition, e));
                }
            };

            List<LoadingEntry> entries = new ArrayList<>();

            for (PluginDefinition definition : list) {
                String path = definition.getPath();
                try {
                    //创建上下文
                    PluginContext context = createContext();
                    //初始化上下文
                    context.set(PluginConcept.class, this);
                    context.initialize();
                    //创建插件
                    Plugin plugin = createPlugin(definition, context);
                    if (plugin == null) {
                        throw new PluginException("Plugin can not create: " + path);
                    }
                    //初始化插件
                    plugin.setConcept(this);

                    context.set(Plugin.class, plugin);
                    eventPublisher.publish(new PluginCreatedEvent(plugin));

                    entries.add(new LoadingEntry(plugin, context));
                } catch (Throwable e) {
                    eventPublisher.publish(new PluginCreateErrorEvent(definition, e));
                    error.accept(definition, e);
                }
            }

            //遍历所有创建成功的插件进行解析，同时处理依赖关系
            while (!entries.isEmpty()) {
                LoadingEntry entry = entries.remove(0);
                doLoad(entry, entries, new Stack<>(), success, error);
            }
        });
    }

    /**
     * 基于依赖关系加载插件
     */
    protected void doLoad(LoadingEntry entry,
                          Collection<LoadingEntry> original,
                          Stack<String> dependencyChain,
                          BiConsumer<PluginDefinition, Plugin> onSuccess,
                          BiConsumer<PluginDefinition, Throwable> onError) {
        Plugin plugin = entry.getPlugin();
        PluginContext context = entry.getContext();
        PluginDefinition definition = plugin.getDefinition();
        String name = plugin.getMetadata().asStandard().getName();
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
                        doLoad(dependency, original, dependencyChain, onSuccess, onError);
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
            //加载插件
            plugin.load(context);
            //获取插件解析配置
            boolean handlerEnabled = plugin.getMetadata().asStandard().getHandler().isEnabled();
            if (handlerEnabled) {
                //解析插件
                PluginHandlerChain chain = obtainHandlerChain(plugin, context);
                chain.next(context);
            }
            eventPublisher.publish(new PluginLoadedEvent(context));
            onSuccess.accept(definition, plugin);
        } catch (Throwable e) {
            eventPublisher.publish(new PluginLoadErrorEvent(context, e));
            onError.accept(definition, e);
        } finally {
            //销毁上下文
            context.destroy();
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
     */
    @Override
    public Plugin unload(@NonNull PluginDefinition definition) {
        return syncWrite(() -> {
            String path = definition.getPath();
            unloading.add(path);
            try {
                Plugin removed = repository.remove(definition);
                if (removed != null) {
                    removed.unload();
                    eventPublisher.publish(new PluginUnloadedEvent(removed));
                }
                return removed;
            } catch (Throwable e) {
                eventPublisher.publish(new PluginUnloadErrorEvent(definition, e));
                throw new PluginUnloadException(definition, e);
            } finally {
                unloading.remove(path);
            }
        });
    }

    @Override
    public boolean isLoaded(PluginDefinition definition) {
        return repository.contains(definition);
    }

    @Override
    public boolean isLoading(PluginDefinition definition) {
        return syncRead(() -> loading.contains(definition.getPath()));
    }

    @Override
    public boolean isUnloading(PluginDefinition definition) {
        return syncRead(() -> unloading.contains(definition.getPath()));
    }

    public List<PluginMetadataFactory> getMetadataFactories() {
        return syncRead(() -> Collections.unmodifiableList(metadataFactories));
    }

    public List<PluginFactory> getFactories() {
        return syncRead(() -> Collections.unmodifiableList(factories));
    }

    public List<PluginHandler> getHandlers() {
        return syncRead(() -> Collections.unmodifiableList(handlers));
    }

    public List<PluginHandlerFactory> getHandlerFactories() {
        return syncRead(() -> Collections.unmodifiableList(handlerFactories));
    }

    public List<PluginInterceptor> getInterceptors() {
        return syncRead(() -> Collections.unmodifiableList(interceptors));
    }

    @Getter
    @RequiredArgsConstructor
    public static class LoadingEntry {

        private final Plugin plugin;

        private final PluginContext context;
    }

    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<B extends AbstractBuilder<B, T>, T extends AbstractPluginConcept> {

        protected PluginPathFactory pathFactory;

        protected PluginContextFactory contextFactory;

        protected PluginTreeFactory treeFactory;

        protected PluginHandlerChainFactory handlerChainFactory;

        protected PluginStorage storage;

        protected PluginRepository repository;

        protected PluginEventPublisher eventPublisher;

        protected PluginLogger logger;

        protected List<PluginMetadataFactory> metadataFactories = new ArrayList<>();

        protected List<PluginFactory> factories = new ArrayList<>();

        protected List<PluginHandler> handlers = new ArrayList<>();

        protected List<PluginHandlerFactory> handlerFactories = new ArrayList<>();

        protected List<PluginInterceptor> interceptors = new ArrayList<>();

        protected List<PluginEventListener> eventListeners = new ArrayList<>();

        /**
         * 设置路径工厂
         */
        public B pathFactory(PluginPathFactory pathFactory) {
            this.pathFactory = pathFactory;
            return (B) this;
        }

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
         * 设置插件存储
         */
        public B storage(PluginStorage storage) {
            this.storage = storage;
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
         * 添加插件元数据工厂
         */
        public B addMetadataFactories(PluginMetadataFactory... factories) {
            return addMetadataFactories(Arrays.asList(factories));
        }

        /**
         * 添加插件元数据工厂
         */
        public B addMetadataFactories(Collection<? extends PluginMetadataFactory> factories) {
            this.metadataFactories.addAll(factories);
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

        /**
         * 添加插件拦截器
         */
        public B addInterceptors(PluginInterceptor... interceptors) {
            return addInterceptors(Arrays.asList(interceptors));
        }

        /**
         * 添加插件拦截器
         */
        public B addInterceptors(Collection<? extends PluginInterceptor> interceptors) {
            this.interceptors.addAll(interceptors);
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

        public T build() {
            T concept = create();
            eventPublisher.register(eventListeners);
            concept.setPathFactory(pathFactory);
            concept.setContextFactory(contextFactory);
            concept.setTreeFactory(treeFactory);
            concept.setHandlerChainFactory(handlerChainFactory);
            concept.setStorage(storage);
            concept.setRepository(repository);
            concept.setEventPublisher(eventPublisher);
            concept.setLogger(logger);
            concept.setMetadataFactories(metadataFactories);
            concept.setInterceptors(interceptors);
            concept.setFactories(factories);
            concept.setHandlers(handlers);
            concept.setHandlerFactories(handlerFactories);
            return concept;
        }

        protected abstract T create();
    }
}
