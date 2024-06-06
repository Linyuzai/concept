package com.github.linyuzai.plugin.jar.read;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.content.ContentReader;
import com.github.linyuzai.plugin.core.read.content.PluginContent;
import com.github.linyuzai.plugin.jar.extension.NestedJarFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@Getter
@RequiredArgsConstructor
public class JarContentReader implements ContentReader {

    private final NestedJarFile jarFile;

    @Override
    public PluginContent read(Object key, PluginContext context) {
        return new JarContent(String.valueOf(key));
    }

    @Getter
    @RequiredArgsConstructor
    public class JarContent implements PluginContent {

        private final String name;

        @Override
        public InputStream getInputStream() throws IOException {
            return jarFile.getInputStream(name);
        }
    }
}
