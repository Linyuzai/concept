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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.*;

/**
 * {@link PluginConcept} 抽象类
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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
     * 插件加载之后是否销毁
     */
    protected final boolean destroyOnLoaded;

    /**
     * 加载插件。
     * 先通过 {@link PluginFactory} 创建插件 {@link Plugin} 并初始化，
     * 通过 {@link PluginContextFactory} 创建上下文 {@link PluginContext} 并初始化，
     * 执行插件解析链 {@link PluginResolver}，
     * 通过 {@link PluginExtractor} 提取插件，
     * 销毁上下文，销毁插件（如需销毁）。
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    @Override
    public Plugin load(Object o) {
        //创建插件
        Plugin plugin = createPlugin(o);
        if (plugin == null) {
            throw new PluginException("Plugin can not created: " + o);
        }

        pluginEventPublisher.publish(new PluginCreatedEvent(plugin));

        //初始化插件
        plugin.initialize();

        pluginEventPublisher.publish(new PluginInitializedEvent(plugin));

        //创建上下文
        PluginContext context = pluginContextFactory.create(plugin, this);

        //在上下文中添加事件发布者
        context.set(PluginEventPublisher.class, pluginEventPublisher);

        //初始化上下文
        context.initialize();

        //解析插件
        new PluginResolverChainImpl(pluginResolvers, pluginFilters).next(context);

        //提取插件
        for (PluginExtractor extractor : pluginExtractors) {
            extractor.extract(context);
        }

        //销毁上下文
        context.destroy();

        if (destroyOnLoaded) {
            //销毁插件
            plugin.destroy();
            pluginEventPublisher.publish(new PluginDestroyedEvent(plugin));
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
    public Plugin createPlugin(Object o) {
        for (PluginFactory factory : pluginFactories) {
            if (factory.support(o, this)) {
                return factory.create(o, this);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        protected PluginContextFactory pluginContextFactory;

        protected PluginEventPublisher pluginEventPublisher;

        protected final Collection<PluginEventListener> pluginEventListeners = new ArrayList<>();

        protected final Collection<PluginFactory> pluginFactories = new ArrayList<>();

        protected final Collection<PluginResolver> pluginResolvers = new ArrayList<>();

        protected final Collection<PluginFilter> pluginFilters = new ArrayList<>();

        protected final Collection<PluginExtractor> pluginExtractors = new ArrayList<>();

        protected boolean destroyOnLoaded = true;

        protected Map<Class<? extends PluginResolver>, Class<? extends PluginResolver>>
                resolverDefaultImpl = new HashMap<>();

        public T contextFactory(PluginContextFactory contextFactory) {
            this.pluginContextFactory = contextFactory;
            return (T) this;
        }

        public T eventPublisher(PluginEventPublisher eventPublisher) {
            this.pluginEventPublisher = eventPublisher;
            return (T) this;
        }

        public T addEventListener(PluginEventListener listener) {
            return addEventListeners(listener);
        }

        public T addEventListeners(PluginEventListener... listeners) {
            return addEventListeners(Arrays.asList(listeners));
        }

        public T addEventListeners(Collection<? extends PluginEventListener> listeners) {
            this.pluginEventListeners.addAll(listeners);
            return (T) this;
        }

        public T addFactory(PluginFactory factory) {
            return addFactories(factory);
        }

        public T addFactories(PluginFactory... factories) {
            return addFactories(Arrays.asList(factories));
        }

        public T addFactories(Collection<? extends PluginFactory> factories) {
            this.pluginFactories.addAll(factories);
            return (T) this;
        }

        public void addResolver(PluginResolver resolver) {
            addResolvers(resolver);
        }

        public T addResolvers(PluginResolver... resolvers) {
            return addResolvers(Arrays.asList(resolvers));
        }

        public T addResolvers(Collection<? extends PluginResolver> resolvers) {
            this.pluginResolvers.addAll(resolvers);
            return (T) this;
        }

        public T mappingResolver(Class<? extends PluginResolver> resolverClass,
                                 Class<? extends PluginResolver> resolverImplClass) {
            resolverDefaultImpl.put(resolverClass, resolverImplClass);
            return (T) this;
        }

        public T addFilter(PluginFilter filter) {
            return addFilters(filter);
        }

        public T addFilters(PluginFilter... filters) {
            return addFilters(Arrays.asList(filters));
        }

        public T addFilters(Collection<? extends PluginFilter> filters) {
            this.pluginFilters.addAll(filters);
            return (T) this;
        }

        public T addExtractor(PluginExtractor extractor) {
            return addExtractors(extractor);
        }

        public T addExtractors(PluginExtractor... extractors) {
            return addExtractors(Arrays.asList(extractors));
        }

        public T addExtractors(Collection<? extends PluginExtractor> extractors) {
            this.pluginExtractors.addAll(extractors);
            return (T) this;
        }

        public T destroyOnLoaded(boolean destroyOnLoaded) {
            this.destroyOnLoaded = destroyOnLoaded;
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

        @SneakyThrows
        private void addResolversDependOnExtractors(Collection<? extends PluginExtractor> extractors) {
            for (PluginExtractor extractor : extractors) {
                Class<? extends PluginResolver>[] dependencies = extractor.dependencies();
                for (Class<? extends PluginResolver> dependency : dependencies) {
                    if (containsResolver(dependency)) {
                        continue;
                    }
                    Class<? extends PluginResolver> implOrDefault =
                            resolverDefaultImpl.getOrDefault(dependency, dependency);
                    PluginResolver resolver = implOrDefault.newInstance();
                    addResolversWithDependencies(Collections.singletonList(resolver));
                }
            }
        }

        @SneakyThrows
        private void addResolversWithDependencies(Collection<? extends PluginResolver> resolvers) {
            if (resolvers.isEmpty()) {
                return;
            }
            pluginResolvers.addAll(resolvers);
            Set<Class<? extends PluginResolver>> unfounded = new HashSet<>();
            for (PluginResolver resolver : resolvers) {
                Class<? extends PluginResolver>[] dependencies = resolver.dependencies();
                for (Class<? extends PluginResolver> dependency : dependencies) {
                    if (containsResolver(dependency)) {
                        continue;
                    }
                    unfounded.add(dependency);
                }
            }
            List<PluginResolver> unfoundedPluginResolvers = new ArrayList<>();
            if (!unfounded.isEmpty()) {
                for (Class<? extends PluginResolver> dependency : unfounded) {
                    Class<? extends PluginResolver> implOrDefault =
                            resolverDefaultImpl.getOrDefault(dependency, dependency);
                    PluginResolver instance = implOrDefault.newInstance();
                    unfoundedPluginResolvers.add(instance);
                }
                addResolversWithDependencies(unfoundedPluginResolvers);
            }
        }

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
