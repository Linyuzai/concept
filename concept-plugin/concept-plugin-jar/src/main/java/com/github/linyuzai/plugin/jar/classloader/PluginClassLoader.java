package com.github.linyuzai.plugin.jar.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public Class<?> findPluginClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
