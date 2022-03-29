package com.github.linyuzai.plugin.jar.classloader;

import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

import java.net.URL;

public interface PluginClassLoaderFactory {

    PluginClassLoader create(URL url, JarPluginConcept concept);
}
