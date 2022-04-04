package com.github.linyuzai.plugin.jar.classloader;

import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;

@Getter
@AllArgsConstructor
public class JarPluginClassLoaderFactory implements PluginClassLoaderFactory {

    private ClassLoader parent;

    @Override
    public PluginClassLoader create(URL url, JarPluginConcept concept) {
        return new JarPluginClassLoader(new URL[]{url}, parent, concept);
    }
}
