package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

import java.io.File;

public class JarFilePluginFactory extends JarPathPluginFactory {

    public JarFilePluginFactory(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof File && super.support(((File) o).getAbsolutePath(), concept);
    }

    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return super.create(((File) o).getAbsolutePath(), concept);
    }
}
