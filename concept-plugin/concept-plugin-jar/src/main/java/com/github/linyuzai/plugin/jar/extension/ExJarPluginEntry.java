package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.core.concept.AbstractPluginEntry;
import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Getter
public class ExJarPluginEntry extends AbstractPluginEntry implements Plugin.Entry {

    private final ExJarFile jarFile;

    private final ExJarEntry jarEntry;

    private final URL url;

    @SneakyThrows
    public ExJarPluginEntry(ExJarFile jarFile, ExJarEntry jarEntry, Plugin plugin) {
        super(jarEntry.getName(), plugin);
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
        this.url = jarEntry.getURL();
    }

    @Override
    public Object getId() {
        return url;
    }

    @Override
    public Plugin.Content getContent() {
        if (jarEntry.isDirectory()) {
            return null;
        }
        return new ExJarEntryContent();
    }

    /**
     * 扩展jar内容
     */
    public class ExJarEntryContent implements Plugin.Content {

        @Override
        public InputStream getInputStream() throws IOException {
            return jarFile.getInputStream(jarEntry);
        }
    }
}
