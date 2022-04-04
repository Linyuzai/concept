package com.github.linyuzai.plugin.jar.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 插件类加载器
 */
public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    /**
     * 查找插件类，只在自身的范围内查找
     *
     * @param name 插件类名
     * @return 插件类
     * @throws ClassNotFoundException 类未找到
     */
    public Class<?> findPluginClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
