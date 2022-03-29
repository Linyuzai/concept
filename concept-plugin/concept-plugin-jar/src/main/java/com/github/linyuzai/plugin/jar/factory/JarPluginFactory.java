package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoader;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.AllArgsConstructor;

import java.net.URL;

@AllArgsConstructor
public abstract class JarPluginFactory implements PluginFactory {

    private PluginClassLoaderFactory pluginClassLoaderFactory;

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return concept instanceof JarPluginConcept && support(o);
    }

    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return doCreate(o, (JarPluginConcept) concept);
    }

    public PluginClassLoader createPluginClassLoader(URL url, JarPluginConcept concept) {
        return pluginClassLoaderFactory.create(url, concept);
    }

    public abstract boolean support(Object o);

    public abstract Plugin doCreate(Object o, JarPluginConcept concept);
}
