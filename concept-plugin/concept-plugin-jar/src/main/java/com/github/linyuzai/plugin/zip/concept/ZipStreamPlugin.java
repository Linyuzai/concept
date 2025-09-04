package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip流插件
 */
@RequiredArgsConstructor
public class ZipStreamPlugin extends AbstractPlugin implements ZipPlugin {

    protected final Supplier<InputStream> supplier;

    @Nullable
    public ZipInputStream getInputStream() {
        InputStream is = supplier.get();
        if (is == null) {
            return null;
        }
        return new ZipInputStream(is);
    }

    @SneakyThrows
    @Override
    public void forEachEntry(PluginContext context, Consumer<Entry> consumer) {
        PluginConcept concept = context.getConcept();
        try (ZipInputStream zis = getInputStream()) {
            if (zis == null) {
                return;
            }
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                String path = concept.getPathFactory().create(getDefinition().getPath(), name);
                byte[] bytes;
                if (entry.isDirectory()) {
                    bytes = null;
                } else {
                    bytes = PluginUtils.read(zis);
                }
                ZipPluginEntry pluginEntry = createPluginEntry(path, name, bytes);
                consumer.accept(pluginEntry);
                zis.closeEntry();
            }
        }
    }

    /**
     * 创建插件条目
     */
    protected ZipPluginEntry createPluginEntry(String path, String name, byte[] bytes) {
        return new ZipStreamPluginEntry(this, name, path, supplier, bytes);
    }
}
