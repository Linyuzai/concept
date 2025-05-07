package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;
import java.util.zip.ZipInputStream;

/**
 * jar流插件
 */
@Getter
@Setter
public class JarStreamPlugin extends ZipStreamPlugin implements JarPlugin {

    private PluginClassLoader pluginClassLoader;

    public JarStreamPlugin(URL url, Supplier<InputStream> supplier) {
        super(url, supplier);
    }

    @Override
    protected JarPluginEntry createPluginEntry(URL url, String name, byte[] bytes) {
        return new JarStreamPluginEntry(name, this, url, supplier, bytes);
    }
}
