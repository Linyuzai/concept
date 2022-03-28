package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPluginConcept;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.resolve.ByteArrayPluginResolver;
import com.github.linyuzai.plugin.core.resolve.PathNamePluginResolver;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;
import com.github.linyuzai.plugin.jar.classloader.JarPluginClassLoader;
import com.github.linyuzai.plugin.jar.extract.JarDynamicPluginExtractor;
import com.github.linyuzai.plugin.jar.factory.JarFilePluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarPathPluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarURLPluginFactory;
import com.github.linyuzai.plugin.jar.resolve.JarByteArrayPluginResolver;
import com.github.linyuzai.plugin.jar.resolve.JarPathNamePluginResolver;
import com.github.linyuzai.plugin.jar.resolve.JarPropertiesPluginResolver;
import lombok.Getter;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JarPluginConcept extends AbstractPluginConcept {

    @Getter
    private final Map<URL, JarPluginClassLoader> classLoaders = new ConcurrentHashMap<>();

    public JarPluginConcept(PluginContextFactory pluginContextFactory,
                            PluginEventPublisher pluginEventPublisher,
                            Collection<PluginFactory> pluginFactories,
                            Collection<PluginResolver> pluginResolvers,
                            Collection<PluginFilter> pluginFilters,
                            Collection<PluginExtractor> pluginExtractors,
                            boolean destroyedOnLoaded) {
        super(pluginContextFactory, pluginEventPublisher, pluginFactories,
                pluginResolvers, pluginFilters, pluginExtractors, destroyedOnLoaded);
    }

    @Override
    public Plugin load(Object o) {
        Plugin plugin = super.load(o);
        if (plugin instanceof JarPlugin) {
            URL url = ((JarPlugin) plugin).getUrl();
            JarPluginClassLoader classLoader = ((JarPlugin) plugin).getClassLoader();
            classLoaders.put(url, classLoader);
        }
        return plugin;
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private ClassLoader classLoader;

        public Builder() {
            mappingResolver(ByteArrayPluginResolver.class, JarByteArrayPluginResolver.class);
            mappingResolver(PathNamePluginResolver.class, JarPathNamePluginResolver.class);
            mappingResolver(PropertiesPluginResolver.class, JarPropertiesPluginResolver.class);
        }

        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder extractTo(Object callback) {
            return addExtractor(new JarDynamicPluginExtractor(callback));
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
                    pluginExtractors,
                    destroyOnLoaded);
        }
    }
}
