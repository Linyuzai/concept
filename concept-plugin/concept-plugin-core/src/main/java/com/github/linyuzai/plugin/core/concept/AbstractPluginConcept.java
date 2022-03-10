package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.DefaultPluginContextFactory;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.extractor.PluginExtractor;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.resolver.PluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChainImpl;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractPluginConcept implements PluginConcept {

    protected final PluginContextFactory pluginContextFactory;

    protected final Collection<PluginFactory> pluginFactories;

    protected final Collection<PluginResolver> pluginResolvers;

    protected final Collection<PluginMatcher> pluginMatchers;

    protected AbstractPluginConcept(PluginContextFactory pluginContextFactory,
                                    Collection<PluginFactory> pluginFactories,
                                    Collection<PluginResolver> pluginResolvers,
                                    Collection<PluginMatcher> pluginMatchers) {
        this.pluginContextFactory = pluginContextFactory;
        this.pluginFactories = pluginFactories;
        this.pluginResolvers = pluginResolvers;
        this.pluginMatchers = pluginMatchers;
    }

    public Plugin doLoad(Object o) {
        Plugin plugin = createPlugin(o);
        if (plugin == null) {
            throw new PluginException("Plugin can not created");
        }
        onPluginCreated(plugin);
        PluginContext context = pluginContextFactory.create(plugin);
        onContextCreated(context);
        new PluginResolverChainImpl(pluginResolvers).next(context);
        return plugin;
    }

    public void onPluginCreated(Plugin plugin) {

    }

    public void onContextCreated(PluginContext context) {

    }

    public Plugin createPlugin(Object o) {
        for (PluginFactory factory : pluginFactories) {
            if (factory.support(o)) {
                return factory.create(o);
            }
        }
        return null;
    }

    public void matchPlugin(Plugin plugin, Object resolved) {
        for (PluginMatcher matcher : pluginMatchers) {
            if (matcher.match(plugin, resolved)) {

            }
        }
    }

    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        private final Set<Class<? extends PluginResolver>> pluginResolverTypes = new HashSet<>();

        protected PluginContextFactory pluginContextFactory;

        protected final List<PluginFactory> pluginFactories = new ArrayList<>();

        protected final List<PluginResolver> pluginResolvers = new ArrayList<>();

        public T contextFactory(PluginContextFactory factory) {
            this.pluginContextFactory = factory;
            return (T) this;
        }

        public T addFactories(PluginFactory... pluginFactories) {
            return addFactories(Arrays.asList(pluginFactories));
        }

        public T addFactories(Collection<? extends PluginFactory> pluginFactories) {
            this.pluginFactories.addAll(pluginFactories);
            return (T) this;
        }

        public void addResolver(PluginResolver resolver) {
            addResolver(resolver, true);
        }

        public T addResolver(PluginResolver resolver, boolean dependence) {
            return addResolvers(Collections.singletonList(resolver), dependence);
        }

        public T addResolvers(PluginResolver... resolvers) {
            return addResolvers(Arrays.asList(resolvers));
        }

        public T addResolvers(Collection<? extends PluginResolver> resolvers) {
            return addResolvers(resolvers, true);
        }

        public T addResolvers(Collection<? extends PluginResolver> resolvers, boolean dependence) {
            return addResolversWithDependencies(resolvers, dependence);
        }

        @SneakyThrows
        private T addResolversWithDependencies(Collection<? extends PluginResolver> resolvers, boolean dependence) {
            if (resolvers.isEmpty()) {
                return (T) this;
            }
            Set<? extends Class<? extends PluginResolver>> classSet = resolvers.stream()
                    .map(PluginResolver::getClass)
                    .collect(Collectors.toSet());
            pluginResolverTypes.addAll(classSet);
            if (dependence) {
                Set<Class<? extends PluginResolver>> unfounded = new HashSet<>();
                for (PluginResolver resolver : resolvers) {
                    Class<? extends PluginResolver>[] dependencies = resolver.dependencies();
                    for (Class<? extends PluginResolver> dependency : dependencies) {
                        if (!pluginResolverTypes.contains(dependency)) {
                            unfounded.add(dependency);
                        }
                    }
                }
                List<PluginResolver> newPluginResolvers = new ArrayList<>();
                if (!unfounded.isEmpty()) {
                    for (Class<? extends PluginResolver> dependency : unfounded) {
                        PluginResolver instance = dependency.newInstance();
                        newPluginResolvers.add(instance);
                    }
                    addResolversWithDependencies(newPluginResolvers, true);
                }
            }
            pluginResolvers.addAll(resolvers);
            return (T) this;
        }

        protected void preBuild() {
            if (pluginContextFactory == null) {
                pluginContextFactory = new DefaultPluginContextFactory();
            }
        }
    }
}
