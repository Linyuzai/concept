package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.DefaultPluginContextFactory;
import com.github.linyuzai.plugin.core.event.*;
import com.github.linyuzai.plugin.core.extractor.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.resolver.PluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChainImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPluginConcept implements PluginConcept {

    protected final PluginContextFactory pluginContextFactory;

    protected final PluginEventPublisher pluginEventPublisher;

    protected final Collection<PluginFactory> pluginFactories;

    protected final Collection<PluginResolver> pluginResolvers;

    protected final Collection<PluginFilter> pluginFilters;

    protected final Collection<PluginExtractor> pluginExtractors;

    @Override
    public Plugin load(Object o) {
        Plugin plugin = createPlugin(o);
        if (plugin == null) {
            throw new PluginException("Plugin can not created");
        }
        PluginContext context = pluginContextFactory.create(plugin, this);

        plugin.initialize();
        context.initialize();

        new PluginResolverChainImpl(pluginResolvers, pluginFilters).next(context);
        for (PluginExtractor extractor : pluginExtractors) {
            extractor.extract(context);
        }

        context.destroy();
        plugin.destroy();

        return plugin;
    }

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

        public T mappingResolver(Class<? extends PluginResolver> resolverClass,
                                 Class<? extends PluginResolver> resolverImplClass) {
            resolverDefaultImpl.put(resolverClass, resolverImplClass);
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
