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
public class ZipPlugin extends AbstractPlugin {

    private final ZipInputStream inputStream;

    private final URL url;

    private final Entry parent;

    @Override
    public Object getId() {
        return url;
    }

    @SneakyThrows
    @Override
    public void collectEntries(PluginContext context, Consumer<Entry> consumer) {
        ZipEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            String name = entry.getName();
            URL id = new URL(url, name);
            byte[] bytes;
            if (entry.isDirectory()) {
                bytes = null;
            } else {
                bytes = PluginUtils.read(inputStream);
            }
            consumer.accept(new ZipPluginEntry(id, name, this, parent, bytes));
            inputStream.closeEntry();
        }
    }

    @Override
    public void onRelease(PluginContext context) {
        try {
            inputStream.close();
        } catch (Throwable ignore) {
        }
    }
}
