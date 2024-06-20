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

@Getter
@RequiredArgsConstructor
public class ZipStreamPlugin extends AbstractPlugin implements ZipPlugin {

    protected final ZipInputStream inputStream;

    protected final URL url;

    protected final Entry parent;

    @Override
    public Object getId() {
        return url;
    }

    @SneakyThrows
    @Override
    public void collectEntries(PluginContext context, Consumer<Entry> consumer) {
        try (ZipInputStream zis = inputStream) {
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
                consumer.accept(createZipPluginEntry(id, name, bytes));
                zis.closeEntry();
            }
        }
    }

    protected ZipPluginEntry createZipPluginEntry(Object id,
                                                  String name,
                                                  byte[] bytes) {
        return new ZipStreamPluginEntry(id, name, this, parent, bytes);
    }
}
