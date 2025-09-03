package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipFilePluginEntry;

import java.util.jar.JarFile;

/**
 * jar文件插件条目
 */
public class JarFilePluginEntry extends ZipFilePluginEntry implements JarPluginEntry {

    public JarFilePluginEntry(Plugin parent, String name, String path, JarFile jarFile) {
        super(parent, name, path, jarFile);
    }
}
