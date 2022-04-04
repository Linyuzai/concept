package com.github.linyuzai.plugin.jar.classloader;

import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

import java.net.URL;

/**
 * 插件类加载器工厂
 */
public interface PluginClassLoaderFactory {

    /**
     * 创建插件类加载器
     *
     * @param url     插件地址 {@link URL}
     * @param concept {@link JarPluginConcept}
     * @return 插件类加载器 {@link PluginClassLoader}
     */
    PluginClassLoader create(URL url, JarPluginConcept concept);
}
