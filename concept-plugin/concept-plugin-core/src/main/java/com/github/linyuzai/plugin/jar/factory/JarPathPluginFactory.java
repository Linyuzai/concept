package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

import java.io.File;

/**
 * 支持文件路径的 {@link JarPlugin} 工厂
 */
public class JarPathPluginFactory extends JarFilePluginFactory {

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof String && ((String) o).endsWith(".jar");
    }

    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return super.create(new File((String) o), concept);
    }
}
