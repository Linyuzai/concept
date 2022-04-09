package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.resolve.ByteArrayPluginResolver;
import com.github.linyuzai.plugin.core.resolve.PathNamePluginResolver;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;
import com.github.linyuzai.plugin.jar.classloader.JarPluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.extract.JarDynamicPluginExtractor;
import com.github.linyuzai.plugin.jar.factory.JarFilePluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarPathPluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarURLPluginFactory;
import com.github.linyuzai.plugin.jar.resolve.JarByteArrayPluginResolver;
import com.github.linyuzai.plugin.jar.resolve.JarPathNamePluginResolver;
import com.github.linyuzai.plugin.jar.resolve.JarPropertiesPluginResolver;
import java.util.Collection;

/**
 * 基于 jar 的 {@link com.github.linyuzai.plugin.core.concept.PluginConcept}
 */
public class JarPluginConcept extends AbstractPluginConcept {

    public JarPluginConcept(PluginContextFactory pluginContextFactory,
                            PluginEventPublisher pluginEventPublisher,
                            Collection<PluginFactory> pluginFactories,
                            Collection<PluginResolver> pluginResolvers,
                            Collection<PluginFilter> pluginFilters,
                            Collection<PluginExtractor> pluginExtractors) {
        super(pluginContextFactory, pluginEventPublisher, pluginFactories,
                pluginResolvers, pluginFilters, pluginExtractors);
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private PluginClassLoaderFactory pluginClassLoaderFactory;

        public Builder() {
            mappingResolver(ByteArrayPluginResolver.class, JarByteArrayPluginResolver.class);
            mappingResolver(PathNamePluginResolver.class, JarPathNamePluginResolver.class);
            mappingResolver(PropertiesPluginResolver.class, JarPropertiesPluginResolver.class);
        }

        /**
         * 设置插件类加载器工厂
         *
         * @param factory 插件类加载器工厂
         * @return {@link Builder}
         */
        public Builder pluginClassLoaderFactory(PluginClassLoaderFactory factory) {
            this.pluginClassLoaderFactory = factory;
            return this;
        }

        /**
         * 动态匹配插件
         *
         * @param callback 回调对象
         * @return {@link Builder}
         */
        public Builder extractTo(Object callback) {
            return addExtractor(new JarDynamicPluginExtractor(callback));
        }

        public JarPluginConcept build() {
            if (pluginClassLoaderFactory == null) {
                pluginClassLoaderFactory = new JarPluginClassLoaderFactory(getClass().getClassLoader());
            }

            addFactory(new JarPathPluginFactory(pluginClassLoaderFactory));
            addFactory(new JarFilePluginFactory(pluginClassLoaderFactory));
            addFactory(new JarURLPluginFactory(pluginClassLoaderFactory));

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
