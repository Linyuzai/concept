package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPluginEntry;

public class JarStreamPluginEntry extends ZipStreamPluginEntry implements JarPluginEntry {

    public JarStreamPluginEntry(Object id, String name, Plugin plugin, Plugin.Entry parent, byte[] bytes) {
        super(id, name, plugin, parent, bytes);
    }
}
