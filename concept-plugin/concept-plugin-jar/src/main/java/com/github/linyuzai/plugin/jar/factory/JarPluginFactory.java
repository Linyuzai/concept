package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoader;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;

/**
 * {@link com.github.linyuzai.plugin.jar.concept.JarPlugin} 工厂
 */
@Getter
@AllArgsConstructor
public abstract class JarPluginFactory implements PluginFactory {

    /**
     * 类加载器工厂
     */
    private PluginClassLoaderFactory pluginClassLoaderFactory;

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return concept instanceof JarPluginConcept && support(o);
    }

    @Override
    public JarPlugin create(Object o, PluginConcept concept) {
        return doCreate(o, (JarPluginConcept) concept);
    }

    /**
     * 创建类加载器
     *
     * @param url     资源 URL
     * @param concept {@link JarPluginConcept}
     * @return 类加载器 {@link PluginClassLoader}
     */
    public PluginClassLoader createPluginClassLoader(URL url, JarPluginConcept concept) {
        return pluginClassLoaderFactory.create(url, concept);
    }

    public abstract boolean support(Object o);

    public abstract JarPlugin doCreate(Object o, JarPluginConcept concept);
}
