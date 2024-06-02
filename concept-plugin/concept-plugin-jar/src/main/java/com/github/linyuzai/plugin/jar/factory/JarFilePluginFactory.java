package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

import java.io.File;

/**
 * 支持 {@link File} 类型的 {@link JarPlugin} 工厂
 */
public class JarFilePluginFactory extends JarPathPluginFactory {

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof File;
    }

    @Override
    public JarPlugin doCreate(Object o, JarPluginConcept concept) {
        return super.doCreate(((File) o).getAbsolutePath(), concept);
    }
}
