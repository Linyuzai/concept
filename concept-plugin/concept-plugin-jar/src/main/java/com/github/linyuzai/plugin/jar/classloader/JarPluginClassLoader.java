package com.github.linyuzai.plugin.jar.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JarPluginClassLoader extends URLClassLoader {

    private final Map<URL, ClassLoader> classLoaders = new ConcurrentHashMap<>();

    public JarPluginClassLoader() {
        super(new URL[]{});
    }

    public JarPluginClassLoader(ClassLoader parent) {
        super(new URL[]{}, parent);
    }

    public Collection<ClassLoader> getClassLoaders() {
        return classLoaders.values();
    }

    public void add(URL url, ClassLoader classLoader) {
        classLoaders.put(url, classLoader);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders.values()) {
            try {
                return classLoader.loadClass(name);
            } catch (Throwable ignore) {
                //find by all class loaders
            }
        }
        throw new ClassNotFoundException(name);
    }
}
