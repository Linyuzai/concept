package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPluginEntry;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * jar流插件条目
 */
public class JarStreamPluginEntry extends ZipStreamPluginEntry implements JarPluginEntry {

    public JarStreamPluginEntry(Plugin parent, String name, String path,
                                Supplier<InputStream> supplier, byte[] bytes) {
        super(parent, name, path, supplier, bytes);
    }
}
