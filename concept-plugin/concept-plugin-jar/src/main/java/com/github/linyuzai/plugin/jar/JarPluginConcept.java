package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.AbstractPluginConcept;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.conflict.PluginConflictStrategy;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginAddedEvent;
import com.github.linyuzai.plugin.core.event.PluginEvent;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.resolver.PluginResolver;
import com.github.linyuzai.plugin.jar.classloader.JarPluginClassLoader;
import com.github.linyuzai.plugin.jar.factory.JarPathPluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarURLPluginFactory;
import com.github.linyuzai.plugin.jar.matcher.JarDynamicPluginMatcher;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JarPluginConcept extends AbstractPluginConcept implements PluginEventListener {

    private final Map<String, JarPluginClassLoader> classLoaders = new ConcurrentHashMap<>();

    public JarPluginConcept(PluginContextFactory pluginContextFactory,
                            PluginConflictStrategy pluginConflictStrategy,
                            PluginEventPublisher pluginEventPublisher,
                            Collection<PluginFactory> pluginFactories,
                            Collection<PluginResolver> pluginResolvers,
                            Collection<PluginFilter> pluginFilters,
                            Collection<PluginMatcher> pluginMatchers) {
        super(pluginContextFactory, pluginConflictStrategy, pluginEventPublisher,
                pluginFactories, pluginResolvers, pluginFilters, pluginMatchers);
        pluginEventPublisher.register(this);
    }

    public Plugin add(URL url) {
        return super.add(url);
    }

    public Plugin add(File file) {
        return super.add(file.getAbsolutePath());
    }

    public Plugin add(String path) {
        return super.add(path);
    }

    public Plugin load(URL url) {
        return super.load(url);
    }

    public Plugin load(File file) {
        return super.load(file.getAbsolutePath());
    }

    public Plugin load(String path) {
        return super.load(path);
    }



    public Collection<JarPluginClassLoader> getClassLoaders() {
        return classLoaders.values();
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof PluginAddedEvent) {
            Plugin plugin = ((PluginEvent) event).getPlugin();
            if (plugin instanceof JarPlugin) {
                JarPluginClassLoader classLoader = ((JarPlugin) plugin).getClassLoader();
                classLoaders.put(plugin.getId(), classLoader);
            }
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private ClassLoader classLoader;

        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder match(Object callback) {
            addMatchers(new JarDynamicPluginMatcher(callback));
            return this;
        }

        public JarPluginConcept build() {

            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }

            addFactory(new JarPathPluginFactory(classLoader));
            addFactory(new JarURLPluginFactory(classLoader));

            preBuild();

            return new JarPluginConcept(
                    pluginContextFactory,
                    pluginConflictStrategy,
                    pluginEventPublisher,
                    pluginFactories,
                    pluginResolvers,
                    pluginFilters,
                    pluginMatchers);
        }
    }
}
