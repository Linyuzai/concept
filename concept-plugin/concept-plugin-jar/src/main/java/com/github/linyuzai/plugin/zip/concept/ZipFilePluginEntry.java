package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPluginEntry;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip文件插件条目
 */
@Getter
public class ZipFilePluginEntry extends AbstractPluginEntry implements ZipPluginEntry {

    protected final ZipFile zipFile;

    protected final URL url;

    public ZipFilePluginEntry(ZipFile zipFile, URL url, String name, Plugin plugin) {
        super(name, plugin);
        this.zipFile = zipFile;
        this.url = url;
    }

    @Override
    public String getPath() {
        return url.toString();
    }

    @Override
    public long getSize() {
        return zipFile.size();
    }

    @Override
    public URL getURL() {
        return url;
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

        @Override
        public InputStream getInputStream() throws IOException {
            ZipEntry entry = zipFile.getEntry(getName());
            if (entry == null) {
                throw new PluginException("Plugin entry not found");
            }
            return zipFile.getInputStream(entry);
        }
    }
}
