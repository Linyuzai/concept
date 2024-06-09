package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
@AllArgsConstructor
public class ZipPluginEntry implements Plugin.Entry {

    private final Object id;

    private final String name;

    private final Plugin plugin;

    private final Plugin.Entry parent;

    private SoftReference<byte[]> bytes;

    @Override
    public Plugin.Content getContent() {
        return new EntryContent();
    }

    public class EntryContent implements Plugin.Content {

        @Override
        public InputStream getInputStream() throws IOException {
            byte[] get = bytes.get();
            if (get != null) {
                return new ByteArrayInputStream(get);
            }
            try (InputStream is = parent.getContent().getInputStream();
                 ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (name.equals(entry.getName())) {
                        byte[] read = PluginUtils.read(zis);
                        bytes = new SoftReference<>(read);
                        return new ByteArrayInputStream(read);
                    }
                }
            }
            throw new PluginException("Plugin entry not found");
        }
    }
}
