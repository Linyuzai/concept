package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
@RequiredArgsConstructor
public class ZipPluginEntry implements Plugin.Entry {

    private final Plugin plugin;

    private final String name;

    private ZipPluginEntry parent;

    private final byte[] bytes;

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public Plugin.Content getContent() {
        return new EntryContent();
    }

    public class EntryContent implements Plugin.Content {

        @Override
        public InputStream getInputStream() throws IOException {
            if (bytes != null) {
                return new ByteArrayInputStream(bytes);
            }
            if (parent != null) {
                InputStream is = parent.getContent().getInputStream();
                ZipInputStream zis = new ZipInputStream(is);
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (name.equals(entry.getName())) {
                        return zis;
                    }
                }
                throw new PluginException("Plugin entry not found");
            }
            throw new PluginException("Plugin entry data error");
        }
    }
}
