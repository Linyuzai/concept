package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;

import java.net.URL;
import java.util.zip.ZipInputStream;

public class JarStreamPlugin extends ZipStreamPlugin implements JarPlugin {

    public JarStreamPlugin(ZipInputStream inputStream, URL url, Entry parent) {
        super(inputStream, url, parent);
    }

    @Override
    protected JarPluginEntry createZipPluginEntry(Object id, String name, byte[] bytes) {
        return new JarStreamPluginEntry(id, name, this, parent, bytes);
    }
}
