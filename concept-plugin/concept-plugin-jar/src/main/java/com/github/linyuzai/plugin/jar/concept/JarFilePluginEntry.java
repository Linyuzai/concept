package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipFilePluginEntry;

import java.net.URL;

public class JarFilePluginEntry extends ZipFilePluginEntry implements JarPluginEntry {

    public JarFilePluginEntry(URL url, String name, Plugin plugin, String path) {
        super(url, name, plugin, path);
    }
}
