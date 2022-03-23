package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.AllArgsConstructor;

import java.net.URL;

@AllArgsConstructor
public class JarURLPluginFactory implements PluginFactory {

    private ClassLoader classLoader;

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof URL && "jar".equals(((URL) o).getProtocol());
    }

    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return new JarPlugin((URL) o, classLoader, (JarPluginConcept) concept);
    }
}
