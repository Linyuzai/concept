package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPluginEntry;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip文件插件条目
 */
@Getter
public class ZipFilePluginEntry extends AbstractPluginEntry implements ZipPluginEntry {

    protected final String path;

    protected final ZipFile zipFile;

    public ZipFilePluginEntry(Plugin parent, String name, String path, ZipFile zipFile) {
        super(parent, name);
        this.path = path;
        this.zipFile = zipFile;
    }

    @Override
    public long getSize() {
        return zipFile.size();
    }

    @Override
    public Plugin.Content getContent() {
        if (isDirectory()) {
            return null;
        }
        return new ZipEntryContent();
    }

    /**
     * zip文件内容
     */
    public class ZipEntryContent implements Plugin.Content {

        @SneakyThrows
        @Override
        public InputStream getInputStream() {
            ZipEntry entry = zipFile.getEntry(getName());
            if (entry == null) {
                throw new PluginException("Plugin entry not found");
            }
            InputStream is = zipFile.getInputStream(entry);
            if (is == null) {
                throw new PluginException("Plugin entry no content");
            }
            return is;
        }
    }
}
