package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

/**
 * 支持文件路径的 {@link JarPlugin} 工厂
 */
public class JarPathPluginFactory extends JarPluginFactory {

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof String && ((String) o).endsWith(".jar");
    }

    @Override
    public JarPlugin doCreate(Object o, JarPluginConcept concept) {
        return new JarPlugin((String) o, getPluginClassLoaderFactory());
    }
}
