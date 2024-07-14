package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPluginEntry;

import java.net.URL;

/**
 * jar流插件条目
 */
public class JarStreamPluginEntry extends ZipStreamPluginEntry implements JarPluginEntry {

    public JarStreamPluginEntry(String name, Plugin plugin, Plugin.Entry parent, URL url, byte[] bytes) {
        super(name, plugin, parent, url, bytes);
    }
}
