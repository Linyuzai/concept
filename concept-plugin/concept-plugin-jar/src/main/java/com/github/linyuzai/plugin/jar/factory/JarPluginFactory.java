package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoader;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

/**
 * {@link com.github.linyuzai.plugin.jar.concept.JarPlugin} 工厂
 */
@Getter
@Setter
public abstract class JarPluginFactory implements PluginFactory {

    /**
     * 类加载器工厂
     */
    private PluginClassLoaderFactory pluginClassLoaderFactory;

    @Override
    public JarPlugin create(Object o, PluginConcept concept) {
        return doCreate(o, (JarPluginConcept) concept);
    }

    public abstract JarPlugin doCreate(Object o, JarPluginConcept concept);
}
