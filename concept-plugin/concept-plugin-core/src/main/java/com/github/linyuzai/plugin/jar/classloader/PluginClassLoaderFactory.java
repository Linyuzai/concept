package com.github.linyuzai.plugin.jar.classloader;

import com.github.linyuzai.plugin.core.concept.PluginConcept;

import java.net.URL;

/**
 * 插件类加载器工厂
 */
public interface PluginClassLoaderFactory {

    /**
     * 创建插件类加载器
     *
     * @param urls     插件地址 {@link URL}
     * @param concept {@link PluginConcept}
     * @return 插件类加载器 {@link PluginClassLoader}
     */
    PluginClassLoader create(URL[] urls, PluginConcept concept);
}
