package com.github.linyuzai.plugin.jar.classloader;

import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.Getter;

import java.net.URL;
import java.util.Collection;

@Getter
public class JarPluginClassLoader extends PluginClassLoader {

    private final JarPluginConcept pluginConcept;

    public JarPluginClassLoader(URL url, ClassLoader parent, JarPluginConcept concept) {
        super(new URL[]{url}, parent);
        this.pluginConcept = concept;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (Throwable e) {
            Collection<PluginClassLoader> classLoaders = pluginConcept.getPluginClassLoaders().values();
            for (PluginClassLoader classLoader : classLoaders) {
                if (classLoader == this) {
                    continue;
                }
                try {
                    return classLoader.findPluginClass(name);
                } catch (Throwable ignore) {
                }
            }
        }
        throw new ClassNotFoundException(name);
    }
}
