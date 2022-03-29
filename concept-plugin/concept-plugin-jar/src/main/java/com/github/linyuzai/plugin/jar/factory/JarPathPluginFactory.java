package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.SneakyThrows;

import java.net.URL;

public class JarPathPluginFactory extends JarPluginFactory {

    public JarPathPluginFactory(PluginClassLoaderFactory pluginClassLoaderFactory) {
        super(pluginClassLoaderFactory);
    }

    @Override
    public boolean support(Object o) {
        return o instanceof String && ((String) o).endsWith(".jar");
    }

    @Override
    public Plugin doCreate(Object o, JarPluginConcept concept) {
        URL url = parseURL((String) o);
        return new JarPlugin(url, createPluginClassLoader(url, concept), concept);
    }

    @SneakyThrows
    public URL parseURL(String jarPath) {
        String url;
        if (jarPath.startsWith("http")) {
            if (jarPath.endsWith("/")) {
                jarPath = jarPath.substring(0, jarPath.length() - 1);
            }
            url = "jar:" + jarPath + "!/";
        } else {
            if (jarPath.startsWith("/")) {
                jarPath = jarPath.substring(1);
            }
            url = "jar:file:/" + jarPath + "!/";
        }
        return new URL(url);
    }
}
