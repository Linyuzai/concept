package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginEntry;
import com.github.linyuzai.plugin.core.read.content.PluginContent;
import com.github.linyuzai.plugin.jar.extension.NestedJarEntry;
import com.github.linyuzai.plugin.jar.extension.NestedJarFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@Getter
@RequiredArgsConstructor
public class JarPluginEntry implements PluginEntry {

    private final Plugin plugin;

    private final NestedJarFile jarFile;

    private final NestedJarEntry jarEntry;

    @Override
    public String getName() {
        return jarEntry.getName();
    }

    @Override
    public PluginContent getContent() {
        return new EntryContent();
    }

    public class EntryContent implements PluginContent {

        @Override
        public InputStream getInputStream() throws IOException {
            return jarFile.getInputStream(jarEntry);
        }
    }
}
