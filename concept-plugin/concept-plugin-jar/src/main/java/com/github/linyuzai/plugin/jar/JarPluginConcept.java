package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.AbstractPluginConcept;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.resolver.PluginResolver;
import com.github.linyuzai.plugin.jar.classloader.JarPluginClassLoader;
import com.github.linyuzai.plugin.jar.factory.JarPathPluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarURLPluginFactory;

import java.io.File;
import java.net.URL;
import java.util.Collection;

public class JarPluginConcept extends AbstractPluginConcept {

    private final JarPluginClassLoader jarPluginClassLoader;

    public JarPluginConcept(PluginContextFactory pluginContextFactory,
                            Collection<PluginFactory> pluginFactories,
                            Collection<PluginResolver> pluginResolvers,
                            Collection<PluginMatcher> pluginMatchers,
                            JarPluginClassLoader jarPluginClassLoader) {
        super(pluginContextFactory, pluginFactories, pluginResolvers, pluginMatchers);
        this.jarPluginClassLoader = jarPluginClassLoader;
    }

    @Override
    public void onPluginCreated(Plugin plugin) {
        if (plugin instanceof JarPlugin) {
            URL url = ((JarPlugin) plugin).getUrl();
            ClassLoader classLoader = ((JarPlugin) plugin).getClassLoader();
            jarPluginClassLoader.add(url, classLoader);
        }
    }

    public Plugin add(URL url) {
        return doLoad(url);
    }

    public Plugin add(File file) {
        return doLoad(file.getAbsolutePath());
    }

    public Plugin add(String path) {
        return doLoad(path);
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private JarPluginClassLoader classLoader;

        public Builder classLoader(JarPluginClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public JarPluginConcept build() {
            preBuild();
            if (classLoader == null) {
                classLoader = new JarPluginClassLoader(getClass().getClassLoader());
            }
            addFactories(new JarPathPluginFactory(classLoader));
            addFactories(new JarURLPluginFactory(classLoader));

            return new JarPluginConcept(
                    pluginContextFactory,
                    pluginFactories,
                    pluginResolvers,
                    null,
                    classLoader);
        }
    }
}
