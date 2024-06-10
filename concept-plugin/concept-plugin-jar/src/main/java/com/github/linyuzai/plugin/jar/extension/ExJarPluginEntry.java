package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;

@Getter
@RequiredArgsConstructor
public class ExJarPluginEntry implements Plugin.Entry {

    private final Plugin plugin;

    private final ExJarFile jarFile;

    private final ExJarEntry jarEntry;

    @SneakyThrows
    @Override
    public Object getId() {
        return jarEntry.getURL();
    }

    @Override
    public String getName() {
        return jarEntry.getName();
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
