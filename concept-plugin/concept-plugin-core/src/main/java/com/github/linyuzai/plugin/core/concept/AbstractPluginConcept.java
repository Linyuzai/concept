package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.*;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChain;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link PluginConcept} 抽象类
 */
@Getter
@Setter
public abstract class AbstractPluginConcept implements PluginConcept {

    public static final String DEFAULT_GROUP = "default";

    /**
     * 上下文工厂
     */
    protected PluginContextFactory contextFactory;

    /**
     * 事件发布者
     */
    protected PluginEventPublisher eventPublisher;

    protected PluginTreeFactory treeFactory;

    protected PluginHandlerChainFactory handlerChainFactory;

    protected Collection<PluginHandler> handlers;

    /**
     * 插件工厂
     */
    protected Collection<PluginFactory> pluginFactories;

    /**
     * 插件提取器
     */
    protected Collection<PluginExtractor> extractors;

    protected volatile PluginHandlerChain handlerChain;

    /**
     * 插件缓存
     */
    protected final Map<Object, Plugin> plugins = new ConcurrentHashMap<>();

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {
        //TODO release plugin
    }

    @Override
    public void addExtractors(PluginExtractor... extractors) {
        addExtractors(Arrays.asList(extractors));
        resetHandlerChain();
    }

    @Override
    public void addExtractors(Collection<? extends PluginExtractor> extractors) {
        this.extractors.addAll(extractors);

    }

    @Override
    public void removeExtractors(PluginExtractor... extractors) {
        removeExtractors(Arrays.asList(extractors));
        resetHandlerChain();
    }

    @Override
    public void removeExtractors(Collection<? extends PluginExtractor> extractors) {
        this.extractors.removeAll(extractors);
    }

    protected PluginHandlerChain getHandlerChain() {
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
        for (PluginFactory factory : pluginFactories) {
            Plugin plugin = factory.create(o, context);
            if (plugin != null) {
                return plugin;
            }
        }
        return null;
    }

    @Override
    public Plugin load(Object o) {
        return load(o, DEFAULT_GROUP);
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
    public Plugin load(Object o, String group) {
        //创建上下文
        PluginContext context = contextFactory.create(this);
        context.set(PluginConcept.class, this);
        //初始化上下文
        context.initialize();

        Plugin plugin = create(o, context);
        if (plugin == null) {
            throw new PluginException("Plugin can not create: " + o);
        }

        plugin.setConcept(this);

        eventPublisher.publish(new PluginCreatedEvent(plugin));

        context.set(Plugin.class, plugin);

        PluginTree tree = treeFactory.create(plugin, this);
        context.set(PluginTree.class, tree);
        context.set(PluginTree.Node.class, tree.getRoot());

        //准备插件
        plugin.prepare(context);
        //在上下文中添加事件发布者
        context.set(PluginEventPublisher.class, eventPublisher);

        eventPublisher.publish(new PluginPreparedEvent(plugin));

        //解析插件
        getHandlerChain().next(context);

        //提取插件
        for (PluginExtractor extractor : extractors) {
            extractor.extract(context);
        }

        plugin.release(context);
        //销毁上下文
        context.destroy();
        eventPublisher.publish(new PluginReleasedEvent(plugin));


        plugins.put(plugin.getId(), plugin);

        eventPublisher.publish(new PluginLoadedEvent(plugin));
        return plugin;
    }

    @Override
    public Plugin unload(Object o) {
        return unload(o, DEFAULT_GROUP);
    }

    /**
     * 卸载插件。
     * 通过插件的 id 或插件本身移除对应的插件
     *
     * @param o 插件源
     */
    @Override
    public Plugin unload(Object o, String group) {
        Plugin plugin = plugins.remove(o);
        if (plugin == null) {
            if (o instanceof Plugin) {
                if (plugins.values().remove(o)) {
                    eventPublisher.publish(new PluginUnloadedEvent((Plugin) o));
                    return (Plugin) o;
                }
            }
        } else {
            eventPublisher.publish(new PluginUnloadedEvent(plugin));
            return plugin;
        }
        return null;
    }

    /**
     * 插件是否加载
     *
     * @param o 插件 id 或插件对象
     * @return 如果加载返回 true 否则返回 false
     */
    @Override
    public boolean isLoaded(Object o) {
        return plugins.containsKey(o) || (o instanceof Plugin && plugins.containsValue(o));
    }

    /**
     * 发布事件
     *
     * @param event 事件
     */
    @Override
    public void publish(Object event) {
        eventPublisher.publish(event);
    }

    /**
     * 获得插件
     *
     * @param id 插件 id
     * @return 插件或 null
     */
    @Override
    public Plugin getPlugin(Object id) {
        return plugins.get(id);
    }

    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<B extends AbstractBuilder<B, T>, T extends AbstractPluginConcept> {

        protected PluginContextFactory contextFactory;

        protected PluginHandlerChainFactory handlerChainFactory;

        protected PluginTreeFactory treeFactory;

        protected PluginEventPublisher eventPublisher;

        protected List<PluginEventListener> eventListeners = new ArrayList<>();

        protected List<PluginFactory> pluginFactories = new ArrayList<>();

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

        public B handlerChainFactory(PluginHandlerChainFactory handlerChainFactory) {
            this.handlerChainFactory = handlerChainFactory;
            return (B) this;
        }

        public B treeFactory(PluginTreeFactory treeFactory) {
            this.treeFactory = treeFactory;
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
            this.pluginFactories.addAll(factories);
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
            concept.setHandlerChainFactory(handlerChainFactory);
            concept.setTreeFactory(treeFactory);
            concept.setEventPublisher(eventPublisher);
            concept.setHandlers(handlers);
            concept.setExtractors(extractors);
            return concept;
        }

        protected abstract T create();

        /**
         * 遍历插件提取器，添加插件提取器依赖的插件解析器
         *
         * @param extractors 插件提取器
         */
        @Deprecated
        @SneakyThrows
        private void addResolversDependOnExtractors(Collection<? extends PluginExtractor> extractors) {
            for (PluginExtractor extractor : extractors) {
                //插件提取器依赖的插件解析器
                Class<? extends PluginHandler>[] dependencies = extractor.getDependencies();
                for (Class<? extends PluginHandler> dependency : dependencies) {
                    //已经存在
                    if (containsResolver(dependency)) {
                        continue;
                    }
                    //获得对应的实现类
                    /*Class<? extends PluginResolver> implOrDefault =
                            resolverDefaultImpl.getOrDefault(dependency, dependency);*/
                    //实例化
                    PluginResolver resolver = (PluginResolver) dependency.newInstance();
                    //添加该插件解析器依赖的解析器
                    addResolversWithDependencies(Collections.singletonList(resolver));
                }
            }
        }

        /**
         * 遍历插件解析器，添加插件解析器依赖的插件解析器。
         *
         * @param resolvers 插件解析器
         */
        @Deprecated
        @SneakyThrows
        private void addResolversWithDependencies(Collection<? extends PluginResolver> resolvers) {
            if (resolvers.isEmpty()) {
                return;
            }
            for (PluginResolver resolver : resolvers) {
                this.handlers.add(0, resolver);
            }

            Set<Class<? extends PluginHandler>> unfounded = new HashSet<>();
            for (PluginResolver resolver : resolvers) {
                //插件解析器依赖的插件解析器
                Class<? extends PluginHandler>[] dependencies = resolver.getDependencies();
                for (Class<? extends PluginHandler> dependency : dependencies) {
                    //已经存在
                    if (containsResolver(dependency)) {
                        continue;
                    }
                    unfounded.add(dependency);
                }
            }
            List<PluginResolver> unfoundedPluginResolvers = new ArrayList<>();
            if (!unfounded.isEmpty()) {
                //遍历需要但是还没有的插件解析器类
                for (Class<? extends PluginHandler> dependency : unfounded) {
                    //获得对应的实现类
                    //实例化
                    PluginResolver instance = (PluginResolver) dependency.newInstance();
                    unfoundedPluginResolvers.add(instance);
                }
                //添加这些新实例化的插件解析器依赖的插件解析器
                addResolversWithDependencies(unfoundedPluginResolvers);
            }
        }

        /**
         * 目标插件解析器类是否已经存在
         *
         * @param target 目标插件解析器类
         * @return 如果已经存在返回 true 否则返回 false
         */
        private boolean containsResolver(Class<? extends PluginHandler> target) {
            for (PluginHandler resolver : handlers) {
                if (target.isInstance(resolver)) {
                    return true;
                }
            }
            return false;
        }
    }
}
