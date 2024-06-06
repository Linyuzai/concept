package com.github.linyuzai.plugin.jar.classloader;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Getter;

import java.net.URL;
import java.util.Collection;

/**
 * 加载 jar 中 {@link Class} 的类加载器
 */
@Getter
public class JarPluginClassLoader extends PluginClassLoader {

    private final PluginConcept concept;

    public JarPluginClassLoader(URL[] urls, ClassLoader parent, PluginConcept concept) {
        super(urls, parent);
        this.concept = concept;
    }

    /**
     * 首先调用当前类加载器的 {@link #findClass(String)}，
     * 如果没有找到，则遍历所有插件的类加载器查找。
     *
     * @param name 类名
     * @return {@link Class} 对象
     * @throws ClassNotFoundException 类未找到
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (Throwable e) {
            Collection<Plugin> plugins = concept.getPlugins().values();
            for (Plugin plugin : plugins) {
                if (plugin instanceof JarPlugin) {
                    //PluginClassLoader classLoader = ((JarPlugin) plugin).getPluginClassLoader();
                    PluginClassLoader classLoader = null;
                    //忽略本身
                    if (classLoader == this) {
                        continue;
                    }
                    try {
                        return classLoader.findPluginClass(name);
                    } catch (Throwable ignore) {
                    }
                }
            }
        }
        throw new ClassNotFoundException(name);
    }
}
