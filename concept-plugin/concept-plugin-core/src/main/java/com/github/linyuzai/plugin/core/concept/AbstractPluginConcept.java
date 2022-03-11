package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.conflict.PluginConflictStrategy;
import com.github.linyuzai.plugin.core.context.DefaultPluginContextFactory;
import com.github.linyuzai.plugin.core.exception.PluginAlreadyLoadedException;
import com.github.linyuzai.plugin.core.exception.PluginNotFoundException;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.resolver.PluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChainImpl;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractPluginConcept implements PluginConcept {

    protected final PluginContextFactory pluginContextFactory;

    protected final PluginConflictStrategy pluginConflictStrategy;

    protected final Collection<PluginFactory> pluginFactories;

    protected final Collection<PluginResolver> pluginResolvers;

    protected final Collection<PluginFilter> pluginFilters;

    protected final Collection<PluginMatcher> pluginMatchers;

    protected final Map<String, Plugin> pluginMap = new ConcurrentHashMap<>();

    protected final Map<String, Plugin> loadedPluginMap = new ConcurrentHashMap<>();

    protected AbstractPluginConcept(PluginContextFactory pluginContextFactory,
                                    PluginConflictStrategy pluginConflictStrategy,
                                    Collection<PluginFactory> pluginFactories,
                                    Collection<PluginResolver> pluginResolvers,
                                    Collection<PluginFilter> pluginFilters,
                                    Collection<PluginMatcher> pluginMatchers) {
        this.pluginContextFactory = pluginContextFactory;
        this.pluginConflictStrategy = pluginConflictStrategy;
        this.pluginFactories = pluginFactories;
        this.pluginResolvers = pluginResolvers;
        this.pluginFilters = pluginFilters;
        this.pluginMatchers = pluginMatchers;
    }

    @Override
    public synchronized Plugin add(Object o) {
        Plugin plugin = createPlugin(o);
        if (plugin == null) {
            throw new PluginException("Plugin can not created");
        }
        String id = plugin.getId();
        Plugin exist = find(id);
        if (exist == null) {
            pluginMap.put(id, plugin);
            onPluginCreated(plugin);
        } else {
            Plugin pluginFromConflict = pluginConflictStrategy.getPlugin(id, exist, plugin);
            if (pluginFromConflict == null) {
                throw new PluginException("Plugin is null on conflict");
            }
            pluginMap.put(id, pluginFromConflict);
            if (plugin != pluginFromConflict) {
                onPluginCreated(pluginFromConflict);
            }
        }
        return plugin;
    }

    public Plugin createPlugin(Object o) {
        for (PluginFactory factory : pluginFactories) {
            if (factory.support(o)) {
                return factory.create(o);
            }
        }
        return null;
    }

    public void onPluginCreated(Plugin plugin) {

    }

    @Override
    public Plugin load(Object o) {
        return load(add(o).getId());
    }

    @Override
    public Plugin load(String id) {
        if (isLoaded(id)) {
            return loadedPluginMap.get(id);
        }
        Plugin plugin = find(id);
        if (plugin == null) {
            throw new PluginNotFoundException(id);
        }

        PluginContext context = pluginContextFactory.create(plugin);
        onContextCreated(context);

        new PluginResolverChainImpl(pluginResolvers, pluginFilters).next(context);
        for (PluginMatcher matcher : pluginMatchers) {
            matcher.match(context);
        }

        loadedPluginMap.put(id, plugin);

        return plugin;
    }

    public void onContextCreated(PluginContext context) {

    }

    @Override
    public Plugin unload(String id) {
        return loadedPluginMap.remove(id);
    }

    @Override
    public Plugin remove(String id) {
        unload(id);
        return pluginMap.remove(id);
    }

    @Override
    public Plugin find(String id) {
        return pluginMap.get(id);
    }

    @Override
    public boolean isExisted(String id) {
        return pluginMap.containsKey(id);
    }

    @Override
    public boolean isLoaded(String id) {
        return loadedPluginMap.containsKey(id);
    }

    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        protected PluginContextFactory pluginContextFactory;

        protected PluginConflictStrategy pluginConflictStrategy;

        protected final Collection<PluginFactory> pluginFactories = new ArrayList<>();

        protected final Collection<PluginResolver> pluginResolvers = new ArrayList<>();

        protected final Collection<PluginFilter> pluginFilters = new ArrayList<>();

        protected final Collection<PluginMatcher> pluginMatchers = new ArrayList<>();

        public T contextFactory(PluginContextFactory contextFactory) {
            this.pluginContextFactory = contextFactory;
            return (T) this;
        }

        public T conflictStrategy(PluginConflictStrategy conflictStrategy) {
            this.pluginConflictStrategy = conflictStrategy;
            return (T) this;
        }

        public T conflictUseCover() {
            return conflictStrategy(new PluginConflictStrategy.Cover());
        }

        public T conflictUseKeep() {
            return conflictStrategy(new PluginConflictStrategy.Keep());
        }

        public T conflictUseError() {
            return conflictStrategy(new PluginConflictStrategy.Error());
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

        public T addMatcher(PluginMatcher matcher) {
            return addMatchers(matcher);
        }

        public T addMatchers(PluginMatcher... matchers) {
            return addMatchers(Arrays.asList(matchers));
        }

        public T addMatchers(Collection<? extends PluginMatcher> matchers) {
            this.pluginMatchers.addAll(matchers);
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

        public T autoLoad() {
            return (T) this;
        }

        protected void preBuild() {
            if (pluginContextFactory == null) {
                pluginContextFactory = new DefaultPluginContextFactory();
            }

            List<PluginResolver> customResolvers = new ArrayList<>(pluginResolvers);
            pluginResolvers.clear();

            addResolversWithDependencies(customResolvers);
            addResolversDependOnMatchers(pluginMatchers);
        }

        @SneakyThrows
        private void addResolversDependOnMatchers(Collection<? extends PluginMatcher> matchers) {
            for (PluginMatcher matcher : matchers) {
                Class<? extends PluginResolver>[] dependencies = matcher.dependencies();
                for (Class<? extends PluginResolver> dependency : dependencies) {
                    if (containsResolver(dependency)) {
                        continue;
                    }
                    PluginResolver resolver = dependency.newInstance();
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
                    PluginResolver instance = dependency.newInstance();
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
