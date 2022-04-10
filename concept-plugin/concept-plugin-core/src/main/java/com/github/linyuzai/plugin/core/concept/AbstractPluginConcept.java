package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.DefaultPluginContextFactory;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.*;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.resolve.PluginResolverChainImpl;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link PluginConcept} 抽象类
 */
@Getter
public abstract class AbstractPluginConcept implements PluginConcept {

    /**
     * 上下文工厂
     */
    protected final PluginContextFactory pluginContextFactory;

    /**
     * 事件发布者
     */
    protected final PluginEventPublisher pluginEventPublisher;

    /**
     * 插件工厂
     */
    protected final Collection<PluginFactory> pluginFactories;

    /**
     * 插件解析器
     */
    protected final Collection<PluginResolver> pluginResolvers;

    /**
     * 插件过滤器
     */
    protected final Collection<PluginFilter> pluginFilters;

    /**
     * 插件提取器
     */
    protected final Collection<PluginExtractor> pluginExtractors;

    /**
     * 插件缓存
     */
    protected final Map<Object, Plugin> plugins = new ConcurrentHashMap<>();

    protected AbstractPluginConcept(PluginContextFactory pluginContextFactory,
                                    PluginEventPublisher pluginEventPublisher,
                                    Collection<PluginFactory> pluginFactories,
                                    Collection<PluginResolver> pluginResolvers,
                                    Collection<PluginFilter> pluginFilters,
                                    Collection<PluginExtractor> pluginExtractors) {
        this.pluginContextFactory = pluginContextFactory;
        this.pluginEventPublisher = pluginEventPublisher;
        this.pluginFactories = pluginFactories;
        this.pluginResolvers = pluginResolvers;
        this.pluginFilters = pluginFilters;
        this.pluginExtractors = pluginExtractors;
    }

    /**
     * 创建插件，如创建失败则抛出异常
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    @Override
    public Plugin create(Object o) {
        if (o instanceof Plugin) {
            return (Plugin) o;
        }
        Plugin plugin = create0(o);
        if (plugin == null) {
            throw new PluginException("Plugin can not create: " + o);
        }
        return plugin;
    }

    /**
     * 创建插件。遍历所有的插件工厂尝试创建插件。
     * 如果没有匹配的工厂则返回 null。
     *
     * @param o 插件源
     * @return 插件 {@link Plugin} 或 null
     */
    private Plugin create0(Object o) {
        for (PluginFactory factory : pluginFactories) {
            if (factory.support(o, this)) {
                return factory.create(o, this);
            }
        }
        return null;
    }

    /**
     * 加载插件。
     * 用过 {@link PluginFactory} 创建插件，
     * 准备插件 {@link Plugin#prepare()}，
     * 通过 {@link PluginContextFactory} 创建上下文 {@link PluginContext} 并初始化，
     * 执行插件解析链 {@link PluginResolver}，
     * 通过 {@link PluginExtractor} 提取插件，
     * 销毁上下文，释放插件资源。
     *
     * @param o 插件源
     */
    @Override
    public Plugin load(Object o) {
        Plugin plugin = create(o);
        if (plugin == null) {
            throw new PluginException("Plugin is null");
        }

        pluginEventPublisher.publish(new PluginCreatedEvent(plugin));

        //准备插件
        plugin.prepare();

        pluginEventPublisher.publish(new PluginPreparedEvent(plugin));

        //创建上下文
        PluginContext context = pluginContextFactory.create(plugin, this);

        //在上下文中添加事件发布者
        context.set(PluginEventPublisher.class, pluginEventPublisher);

        //初始化上下文
        context.initialize();

        //解析插件
        new PluginResolverChainImpl(new ArrayList<>(pluginResolvers), new ArrayList<>(pluginFilters))
                .next(context);

        //提取插件
        for (PluginExtractor extractor : pluginExtractors) {
            extractor.extract(context);
        }

        //销毁上下文
        context.destroy();

        plugin.release();
        pluginEventPublisher.publish(new PluginReleasedEvent(plugin));

        plugins.put(plugin.getId(), plugin);

        pluginEventPublisher.publish(new PluginLoadedEvent(plugin));

        return plugin;
    }

    /**
     * 卸载插件。
     * 通过插件的 id 或插件本身移除对应的插件
     *
     * @param o 插件源
     */
    @Override
    public Plugin unload(Object o) {
        Plugin plugin = plugins.remove(o);
        if (plugin == null) {
            if (o instanceof Plugin) {
                if (plugins.values().remove(o)) {
                    pluginEventPublisher.publish(new PluginUnloadedEvent((Plugin) o));
                    return (Plugin) o;
                }
            }
        } else {
            pluginEventPublisher.publish(new PluginUnloadedEvent(plugin));
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
    public boolean isLoad(Object o) {
        return plugins.containsKey(o) || (o instanceof Plugin && plugins.containsValue(o));
    }

    /**
     * 发布事件
     *
     * @param event 事件
     */
    @Override
    public void publish(Object event) {
        pluginEventPublisher.publish(event);
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
    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        protected PluginContextFactory pluginContextFactory;

        protected PluginEventPublisher pluginEventPublisher;

        protected final List<PluginEventListener> pluginEventListeners = new ArrayList<>();

        protected final List<PluginFactory> pluginFactories = new ArrayList<>();

        protected final List<PluginResolver> pluginResolvers = new ArrayList<>();

        protected final List<PluginFilter> pluginFilters = new ArrayList<>();

        protected final List<PluginExtractor> pluginExtractors = new ArrayList<>();

        protected Map<Class<? extends PluginResolver>, Class<? extends PluginResolver>>
                resolverDefaultImpl = new HashMap<>();

        /**
         * 设置上下文工厂
         *
         * @param contextFactory 上下文工厂
         * @return {@link T}
         */
        public T contextFactory(PluginContextFactory contextFactory) {
            this.pluginContextFactory = contextFactory;
            return (T) this;
        }

        /**
         * 设置事件发布者
         *
         * @param eventPublisher 事件发布者
         * @return {@link T}
         */
        public T eventPublisher(PluginEventPublisher eventPublisher) {
            this.pluginEventPublisher = eventPublisher;
            return (T) this;
        }

        /**
         * 添加事件监听器
         *
         * @param listener 事件监听器
         * @return {@link T}
         */
        public T addEventListener(PluginEventListener listener) {
            return addEventListeners(listener);
        }

        /**
         * 添加事件监听器
         *
         * @param listeners 事件监听器
         * @return {@link T}
         */
        public T addEventListeners(PluginEventListener... listeners) {
            return addEventListeners(Arrays.asList(listeners));
        }

        /**
         * 添加事件监听器
         *
         * @param listeners 事件监听器
         * @return {@link T}
         */
        public T addEventListeners(Collection<? extends PluginEventListener> listeners) {
            this.pluginEventListeners.addAll(listeners);
            return (T) this;
        }

        /**
         * 添加插件工厂
         *
         * @param factory 插件工厂
         * @return {@link T}
         */
        public T addFactory(PluginFactory factory) {
            return addFactories(factory);
        }

        /**
         * 添加插件工厂
         *
         * @param factories 插件工厂
         * @return {@link T}
         */
        public T addFactories(PluginFactory... factories) {
            return addFactories(Arrays.asList(factories));
        }

        /**
         * 添加插件工厂
         *
         * @param factories 插件工厂
         * @return {@link T}
         */
        public T addFactories(Collection<? extends PluginFactory> factories) {
            this.pluginFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加插件解析器
         *
         * @param resolver 插件解析器
         * @return {@link T}
         */
        public T addResolver(PluginResolver resolver) {
            return addResolvers(resolver);
        }

        /**
         * 添加插件解析器
         *
         * @param resolvers 插件解析器
         * @return {@link T}
         */
        public T addResolvers(PluginResolver... resolvers) {
            return addResolvers(Arrays.asList(resolvers));
        }

        /**
         * 添加插件解析器
         *
         * @param resolvers 插件解析器
         * @return {@link T}
         */
        public T addResolvers(Collection<? extends PluginResolver> resolvers) {
            this.pluginResolvers.addAll(resolvers);
            return (T) this;
        }

        /**
         * 添加插件解析器实现映射
         *
         * @param resolverClass     插件解析器类
         * @param resolverImplClass 插件解析器实现类
         * @return {@link T}
         */
        public T mappingResolver(Class<? extends PluginResolver> resolverClass,
                                 Class<? extends PluginResolver> resolverImplClass) {
            resolverDefaultImpl.put(resolverClass, resolverImplClass);
            return (T) this;
        }

        /**
         * 添加插件过滤器
         *
         * @param filter 插件过滤器
         * @return {@link T}
         */
        public T addFilter(PluginFilter filter) {
            return addFilters(filter);
        }

        /**
         * 添加插件过滤器
         *
         * @param filters 插件过滤器
         * @return {@link T}
         */
        public T addFilters(PluginFilter... filters) {
            return addFilters(Arrays.asList(filters));
        }

        /**
         * 添加插件过滤器
         *
         * @param filters 插件过滤器
         * @return {@link T}
         */
        public T addFilters(Collection<? extends PluginFilter> filters) {
            this.pluginFilters.addAll(filters);
            return (T) this;
        }

        /**
         * 添加插件提取器
         *
         * @param extractor 插件提取器
         * @return {@link T}
         */
        public T addExtractor(PluginExtractor extractor) {
            return addExtractors(extractor);
        }

        /**
         * 添加插件提取器
         *
         * @param extractors 插件提取器
         * @return {@link T}
         */
        public T addExtractors(PluginExtractor... extractors) {
            return addExtractors(Arrays.asList(extractors));
        }

        /**
         * 添加插件提取器
         *
         * @param extractors 插件提取器
         * @return {@link T}
         */
        public T addExtractors(Collection<? extends PluginExtractor> extractors) {
            this.pluginExtractors.addAll(extractors);
            return (T) this;
        }

        protected void preBuild() {
            if (pluginContextFactory == null) {
                pluginContextFactory = new DefaultPluginContextFactory();
            }

            if (pluginEventPublisher == null) {
                pluginEventPublisher = new DefaultPluginEventPublisher();
            }

            pluginEventPublisher.register(pluginEventListeners);

            List<PluginResolver> customResolvers = new ArrayList<>(pluginResolvers);
            pluginResolvers.clear();

            addResolversWithDependencies(customResolvers);
            addResolversDependOnExtractors(pluginExtractors);
        }

        /**
         * 遍历插件提取器，添加插件提取器依赖的插件解析器
         *
         * @param extractors 插件提取器
         */
        @SneakyThrows
        private void addResolversDependOnExtractors(Collection<? extends PluginExtractor> extractors) {
            for (PluginExtractor extractor : extractors) {
                //插件提取器依赖的插件解析器
                Class<? extends PluginResolver>[] dependencies = extractor.dependencies();
                for (Class<? extends PluginResolver> dependency : dependencies) {
                    //已经存在
                    if (containsResolver(dependency)) {
                        continue;
                    }
                    //获得对应的实现类
                    Class<? extends PluginResolver> implOrDefault =
                            resolverDefaultImpl.getOrDefault(dependency, dependency);
                    //实例化
                    PluginResolver resolver = implOrDefault.newInstance();
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
        @SneakyThrows
        private void addResolversWithDependencies(Collection<? extends PluginResolver> resolvers) {
            if (resolvers.isEmpty()) {
                return;
            }
            for (PluginResolver resolver : resolvers) {
                pluginResolvers.add(0, resolver);
            }

            Set<Class<? extends PluginResolver>> unfounded = new HashSet<>();
            for (PluginResolver resolver : resolvers) {
                //插件解析器依赖的插件解析器
                Class<? extends PluginResolver>[] dependencies = resolver.dependencies();
                for (Class<? extends PluginResolver> dependency : dependencies) {
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
                for (Class<? extends PluginResolver> dependency : unfounded) {
                    //获得对应的实现类
                    Class<? extends PluginResolver> implOrDefault =
                            resolverDefaultImpl.getOrDefault(dependency, dependency);
                    //实例化
                    PluginResolver instance = implOrDefault.newInstance();
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
        private boolean containsResolver(Class<? extends PluginResolver> target) {
            for (PluginResolver resolver : pluginResolvers) {
                if (target.isInstance(resolver)) {
                    return true;
                }
            }
            return false;
        }
    }
}
