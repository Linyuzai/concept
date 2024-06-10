package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
public class ZipPluginEntry implements Plugin.Entry {

    private final Object id;

    private final String name;

    private final Plugin plugin;

    private final Plugin.Entry parent;

    private Reference<byte[]> reference;

    public ZipPluginEntry(Object id,
                          String name,
                          Plugin plugin,
                          Plugin.Entry parent,
                          byte[] bytes) {
        this.id = id;
        this.name = name;
        this.plugin = plugin;
        this.parent = parent;
        if (!isDirectory()) {
            this.reference = createReference(bytes);
        }
    }

    @Override
    public Plugin.Content getContent() {
        if (isDirectory()) {
            return null;
        }
        return new EntryContent();
    }

    protected boolean isDirectory() {
        return name.endsWith("/");
    }

    protected Reference<byte[]> createReference(byte[] bytes) {
        return new SoftReference<>(bytes);
    }

    public class EntryContent implements Plugin.Content {

        @Override
        public InputStream getInputStream() throws IOException {
            byte[] bytes = reference.get();
            if (bytes != null) {
                return new ByteArrayInputStream(bytes);
            }
            try (InputStream is = parent.getContent().getInputStream();
                 ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (Objects.equals(name, entry.getName())) {
                        byte[] read = PluginUtils.read(zis);
                        reference = new SoftReference<>(read);
                        return new ByteArrayInputStream(read);
                    }
                }
            }
            throw new PluginException("Plugin entry not found");
        }
    }
}
