package com.github.linyuzai.plugin.jar.classloader;

import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.Getter;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

@Getter
public class JarPluginClassLoader extends URLClassLoader {

    private final JarPluginConcept pluginConcept;

    public JarPluginClassLoader(URL url, ClassLoader parent, JarPluginConcept concept) {
        super(new URL[]{url}, parent);
        this.pluginConcept = concept;
    }

    public Class<?> superFindClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (Throwable e) {
            Collection<JarPluginClassLoader> classLoaders = pluginConcept.getClassLoaders().values();
            for (JarPluginClassLoader classLoader : classLoaders) {
                if (classLoader == this) {
                    continue;
                }
                try {
                    return classLoader.superFindClass(name);
                } catch (Throwable ignore) {
                }
            }
        }
        throw new ClassNotFoundException(name);
    }
}
