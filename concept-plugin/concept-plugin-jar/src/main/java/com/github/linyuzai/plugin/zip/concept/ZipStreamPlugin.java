package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip流插件
 */
@RequiredArgsConstructor
public class ZipStreamPlugin extends AbstractPlugin implements ZipPlugin {

    /**
     * zip流
     */
    @Getter
    protected final ZipInputStream zipInputStream;

    protected final URL url;

    /**
     * 父条目
     */
    @Getter
    protected final Entry parent;

    @Override
    public Object getId() {
        return url;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @SneakyThrows
    @Override
    public void forEachEntry(PluginContext context, Consumer<Entry> consumer) {
        try (ZipInputStream zis = zipInputStream) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                URL id = new URL(url, name);
                byte[] bytes;
                if (entry.isDirectory()) {
                    bytes = null;
                } else {
                    bytes = PluginUtils.read(zis);
                }
                ZipPluginEntry pluginEntry = createPluginEntry(id, name, bytes);
                consumer.accept(pluginEntry);
                zis.closeEntry();
            }
        }
    }

    /**
     * 创建插件条目
     */
    protected ZipPluginEntry createPluginEntry(URL url, String name, byte[] bytes) {
        return new ZipStreamPluginEntry(name, this, parent, url, bytes);
    }
}
