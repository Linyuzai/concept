package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipFilePluginEntry;

import java.net.URL;
import java.util.jar.JarFile;

/**
 * jar文件插件条目
 */
public class JarFilePluginEntry extends ZipFilePluginEntry implements JarPluginEntry {

    public JarFilePluginEntry(JarFile jarFile, URL url, String name, Plugin plugin) {
        super(jarFile, url, name, plugin);
    }
}
