package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPluginEntry;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

/**
 * jar流插件条目
 */
public class JarStreamPluginEntry extends ZipStreamPluginEntry implements JarPluginEntry {

    public JarStreamPluginEntry(String name, Plugin plugin, URL url,
                                Supplier<InputStream> supplier, byte[] bytes) {
        super(name, plugin, url, supplier, bytes);
    }
}
