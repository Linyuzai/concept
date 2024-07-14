package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;

import java.net.URL;
import java.util.zip.ZipInputStream;

/**
 * jar流插件
 */
public class JarStreamPlugin extends ZipStreamPlugin implements JarPlugin {

    public JarStreamPlugin(ZipInputStream inputStream, URL url, Entry parent) {
        super(inputStream, url, parent);
    }

    @Override
    protected JarPluginEntry createPluginEntry(URL url, String name, byte[] bytes) {
        return new JarStreamPluginEntry(name, this, parent, url, bytes);
    }

    @Override
    public PluginClassLoader getPluginClassLoader() {
        return null;
    }
}
