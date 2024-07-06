package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExJarPluginEntry implements Plugin.Entry {

    @Getter
    private final Plugin plugin;

    @Getter
    private final ExJarFile jarFile;

    @Getter
    private final ExJarEntry jarEntry;

    private final URL url;

    @SneakyThrows
    public ExJarPluginEntry(Plugin plugin, ExJarFile jarFile, ExJarEntry jarEntry) {
        this.plugin = plugin;
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
        this.url = jarEntry.getURL();
    }

    @Override
    public Object getId() {
        return url;
    }

    @Override
    public String getName() {
        return jarEntry.getName();
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public Plugin.Content getContent() {
        if (jarEntry.isDirectory()) {
            return null;
        }
        return new EntryContent();
    }

    public class EntryContent implements Plugin.Content {

        @Override
        public InputStream getInputStream() throws IOException {
            return jarFile.getInputStream(jarEntry);
        }
    }
}
