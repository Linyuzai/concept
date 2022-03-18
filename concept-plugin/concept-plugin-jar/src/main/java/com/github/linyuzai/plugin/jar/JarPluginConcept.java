package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.AbstractPluginConcept;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.resolve.BytesPluginResolver;
import com.github.linyuzai.plugin.core.resolve.FileNamePluginResolver;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;
import com.github.linyuzai.plugin.jar.classloader.JarPluginClassLoader;
import com.github.linyuzai.plugin.jar.factory.JarFilePluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarPathPluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarURLPluginFactory;
import com.github.linyuzai.plugin.jar.match.JarDynamicPluginExtractor;
import com.github.linyuzai.plugin.jar.resolve.JarBytesPluginResolver;
import com.github.linyuzai.plugin.jar.resolve.JarFileNamePluginResolver;
import com.github.linyuzai.plugin.jar.resolve.JarPropertiesPluginResolver;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

public class JarPluginConcept extends AbstractPluginConcept {

    private final Collection<JarPluginClassLoader> classLoaders =
            Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    public JarPluginConcept(PluginContextFactory pluginContextFactory,
                            PluginEventPublisher pluginEventPublisher,
                            Collection<PluginFactory> pluginFactories,
                            Collection<PluginResolver> pluginResolvers,
                            Collection<PluginFilter> pluginFilters,
                            Collection<PluginExtractor> pluginExtractors) {
        super(pluginContextFactory, pluginEventPublisher, pluginFactories,
                pluginResolvers, pluginFilters, pluginExtractors);
    }

    public Collection<JarPluginClassLoader> getClassLoaders() {
        return classLoaders;
    }

    @Override
    public Plugin load(Object o) {
        Plugin plugin = super.load(o);
        if (plugin instanceof JarPlugin) {
            JarPluginClassLoader classLoader = ((JarPlugin) plugin).getClassLoader();
            classLoaders.add(classLoader);
        }
        return plugin;
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private ClassLoader classLoader;

        public Builder() {
            mappingResolver(BytesPluginResolver.class, JarBytesPluginResolver.class);
            mappingResolver(FileNamePluginResolver.class, JarFileNamePluginResolver.class);
            mappingResolver(PropertiesPluginResolver.class, JarPropertiesPluginResolver.class);
        }

        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder extractTo(Object callback) {
            return addExtractors(new JarDynamicPluginExtractor(callback));
        }

        public JarPluginConcept build() {
            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }

            addFactory(new JarPathPluginFactory(classLoader));
            addFactory(new JarFilePluginFactory(classLoader));
            addFactory(new JarURLPluginFactory(classLoader));

            preBuild();

            return new JarPluginConcept(
                    pluginContextFactory,
                    pluginEventPublisher,
                    pluginFactories,
                    pluginResolvers,
                    pluginFilters,
                    pluginExtractors);
        }
    }
}
