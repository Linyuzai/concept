package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.AbstractPluginConcept;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.conflict.PluginConflictStrategy;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
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

public class JarPluginConcept extends AbstractPluginConcept {

    private final JarPluginClassLoader jarPluginClassLoader;

    public JarPluginConcept(PluginContextFactory pluginContextFactory,
                            PluginConflictStrategy pluginConflictStrategy,
                            Collection<PluginFactory> pluginFactories,
                            Collection<PluginResolver> pluginResolvers,
                            Collection<PluginFilter> pluginFilters,
                            Collection<PluginMatcher> pluginMatchers,
                            JarPluginClassLoader jarPluginClassLoader) {
        super(pluginContextFactory, pluginConflictStrategy, pluginFactories,
                pluginResolvers, pluginFilters, pluginMatchers);
        this.jarPluginClassLoader = jarPluginClassLoader;
    }

    @Override
    public void onPluginCreated(Plugin plugin) {
        if (plugin instanceof JarPlugin) {
            ClassLoader classLoader = ((JarPlugin) plugin).getClassLoader();
            jarPluginClassLoader.add(plugin.getId(), classLoader);
        }
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

    public static class Builder extends AbstractBuilder<Builder> {

        private JarPluginClassLoader classLoader;

        public Builder classLoader(JarPluginClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder match(Object callback) {
            addMatchers(new JarDynamicPluginMatcher(callback));
            return this;
        }

        public JarPluginConcept build() {

            preBuild();

            if (classLoader == null) {
                classLoader = new JarPluginClassLoader(getClass().getClassLoader());
            }

            addFactory(new JarPathPluginFactory(classLoader));
            addFactory(new JarURLPluginFactory(classLoader));

            return new JarPluginConcept(
                    pluginContextFactory,
                    pluginConflictStrategy,
                    pluginFactories,
                    pluginResolvers,
                    pluginFilters,
                    pluginMatchers,
                    classLoader);
        }
    }
}
