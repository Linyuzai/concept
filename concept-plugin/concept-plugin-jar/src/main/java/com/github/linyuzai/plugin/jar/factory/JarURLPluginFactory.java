package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

import java.net.URL;

/**
 * 支持 {@link URL} 类型的 {@link JarPlugin} 工厂
 */
public class JarURLPluginFactory extends JarPluginFactory {

    public JarURLPluginFactory(PluginClassLoaderFactory pluginClassLoaderFactory) {
        super(pluginClassLoaderFactory);
    }

    @Override
    public boolean support(Object o) {
        return o instanceof URL && "jar".equals(((URL) o).getProtocol());
    }

    @Override
    public JarPlugin doCreate(Object o, JarPluginConcept concept) {
        URL url = (URL) o;
        return new JarPlugin(url, createPluginClassLoader(url, concept));
    }
}
