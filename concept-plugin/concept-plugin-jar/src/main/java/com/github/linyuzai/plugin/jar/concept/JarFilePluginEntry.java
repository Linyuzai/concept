package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipFilePluginEntry;

public class JarFilePluginEntry extends ZipFilePluginEntry implements JarPluginEntry {

    public JarFilePluginEntry(Object id, String name, Plugin plugin, String path) {
        super(id, name, plugin, path);
    }
}
