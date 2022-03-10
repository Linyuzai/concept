package com.github.linyuzai.plugin.jar.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class DynamicParentClassLoader extends URLClassLoader {

    private final JarPluginClassLoader jarPluginClassLoader;

    private final Collection<ClassLoader> parents = new CopyOnWriteArrayList<>();

    public DynamicParentClassLoader(URL url, JarPluginClassLoader classLoader) {
        super(new URL[]{url}, classLoader.getParent());
        this.jarPluginClassLoader = classLoader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (Throwable e) {
            for (ClassLoader classLoader : parents) {
                try {
                    return classLoader.loadClass(name);
                } catch (Throwable ignore) {
                }
            }
            Collection<ClassLoader> classLoaders = jarPluginClassLoader.getClassLoaders();
            for (ClassLoader classLoader : classLoaders) {
                if (classLoader == this || parents.contains(classLoader)) {
                    continue;
                }
                try {
                    Class<?> clazz = classLoader.loadClass(name);
                    parents.add(classLoader);
                    return clazz;
                } catch (Throwable ignore) {
                }
            }
        }
        throw new ClassNotFoundException(name);
    }
}
