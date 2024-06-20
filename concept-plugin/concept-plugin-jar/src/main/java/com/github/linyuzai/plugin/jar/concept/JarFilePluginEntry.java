package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipFilePluginEntry;

import java.io.File;

public class JarFilePluginEntry extends ZipFilePluginEntry implements JarPluginEntry {

    public JarFilePluginEntry(Object id, String name, Plugin plugin, File file) {
        super(id, name, plugin, file);
    }
}
