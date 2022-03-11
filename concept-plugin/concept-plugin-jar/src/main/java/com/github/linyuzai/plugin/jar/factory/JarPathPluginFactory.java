package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.JarPluginConcept;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;

@AllArgsConstructor
public class JarPathPluginFactory implements PluginFactory {

    private ClassLoader classLoader;

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof String && ((String) o).endsWith(".jar");
    }

    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return new JarPlugin(parseURL((String) o), classLoader, (JarPluginConcept) concept);
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
