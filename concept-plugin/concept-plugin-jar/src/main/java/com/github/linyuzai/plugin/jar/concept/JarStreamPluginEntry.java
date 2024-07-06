package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPluginEntry;

import java.net.URL;

public class JarStreamPluginEntry extends ZipStreamPluginEntry implements JarPluginEntry {

    public JarStreamPluginEntry(URL url, String name, Plugin plugin, Plugin.Entry parent, byte[] bytes) {
        super(url, name, plugin, parent, bytes);
    }
}
