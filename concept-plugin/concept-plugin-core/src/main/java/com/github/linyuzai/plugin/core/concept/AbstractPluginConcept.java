package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.adapter.PluginAdapter;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.extractor.PluginExtractor;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.resolver.PluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChainImpl;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractPluginConcept implements PluginConcept {

    private PluginContextFactory pluginContextFactory;

    private Collection<PluginAdapter> pluginAdapters;

    private Collection<PluginResolver> pluginResolvers;

    private Collection<PluginMatcher> pluginMatchers;

    private Collection<PluginExtractor> pluginExtractors;

    public void doLoad(Object o) {
        Plugin plugin = adaptPlugin(o);
        PluginContext context = pluginContextFactory.create(plugin);
        onContextCreated(context);
        new PluginResolverChainImpl(pluginResolvers, pluginExtractors).next(context);
    }

    public void onContextCreated(PluginContext context) {

    }

    public Plugin adaptPlugin(Object plugin) {
        for (PluginAdapter adapter : pluginAdapters) {
            Plugin adapt = adapter.adapt(plugin);
            if (adapt != null) {
                return adapt;
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

    public void extractPlugin(Plugin plugin, Object matched) {
        for (PluginExtractor extractor : pluginExtractors) {
            //extractor.extract(plugin, matched);
        }
    }

    public static class Builder {

        private final Set<Class<? extends PluginResolver>> pluginResolverTypes = new HashSet<>();

        private final List<PluginResolver> pluginResolvers = new ArrayList<>();

        public void addResolver(PluginResolver resolver) {
            addResolver(resolver, true);
        }

        public void addResolver(PluginResolver resolver, boolean dependence) {
            addResolvers(Collections.singletonList(resolver), dependence);
        }

        public void addResolvers(PluginResolver... resolvers) {
            addResolvers(Arrays.asList(resolvers));
        }

        public void addResolvers(Collection<? extends PluginResolver> resolvers) {
            addResolvers(resolvers, true);
        }

        public void addResolvers(Collection<? extends PluginResolver> resolvers, boolean dependence) {
            addResolversWithDependencies(resolvers, dependence);
        }

        @SneakyThrows
        private void addResolversWithDependencies(Collection<? extends PluginResolver> resolvers, boolean dependence) {
            if (resolvers.isEmpty()) {
                return;
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
        }

        @Deprecated
        @SneakyThrows
        private void dependence(Class<? extends PluginResolver> resolverType,
                                Set<Class<? extends PluginResolver>> resolverTypes,
                                Collection<PluginResolver> container) {
            if (resolverTypes.contains(resolverType)) {
                return;
            }
            PluginResolver resolver = resolverType.newInstance();
            Class<? extends PluginResolver>[] dependencies = resolver.dependencies();
            for (Class<? extends PluginResolver> dependency : dependencies) {
                dependence(dependency, resolverTypes, container);
            }
            resolverTypes.add(resolverType);
            container.add(resolver);
        }
    }
}
