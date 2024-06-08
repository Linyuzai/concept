package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public Collection<Entry> collectEntries(PluginContext context) {
        List<Entry> entries = new ArrayList<>();
        ZipEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            byte[] bytes = PluginUtils.read(inputStream);
            entries.add(new ZipPluginEntry(this, name, bytes));
            inputStream.closeEntry();
        }
        return entries;
    }

    @SneakyThrows
    @Override
    public void onRelease(PluginContext context) {
        inputStream.close();
    }

    @Override
    public String toString() {
        return "ZipPlugin(" + url + ")";
    }

    public static class Mode {

        public static final String MEMORY = "MEMORY";

        public static final String FILE = "FILE";
    }
}
