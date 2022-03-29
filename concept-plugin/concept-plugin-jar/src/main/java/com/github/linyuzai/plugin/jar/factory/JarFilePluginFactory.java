package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

import java.io.File;

public class JarFilePluginFactory extends JarPathPluginFactory {

    public JarFilePluginFactory(PluginClassLoaderFactory factory) {
        super(factory);
    }

    @Override
    public boolean support(Object o) {
        return o instanceof File;
    }

    @Override
    public Plugin doCreate(Object o, JarPluginConcept concept) {
        return super.doCreate(((File) o).getAbsolutePath(), concept);
    }
}
