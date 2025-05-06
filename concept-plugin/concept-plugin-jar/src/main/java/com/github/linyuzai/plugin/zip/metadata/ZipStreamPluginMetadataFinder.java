package com.github.linyuzai.plugin.zip.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.AbstractPluginMetadataFinder;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFinder;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPluginEntry;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public abstract class ZipStreamPluginMetadataFinder extends ZipPluginMetadataFinder {

    @SneakyThrows
    @Override
    public PluginMetadata find(Object source, PluginContext context) {
        try (ZipInputStream zis = getZipInputStream(source, context)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                String name = zipEntry.getName();
                if (!zipEntry.isDirectory()) {
                    Adapter adapter = getAdapter(name);
                    if (adapter != null) {
                        return adapter.adapt(zis);
                    }
                }
                zis.closeEntry();
            }
        }
        return new PropertiesMetadata(new Properties());
    }

    protected abstract ZipInputStream getZipInputStream(Object source, PluginContext context) throws IOException;

    protected boolean isSupported(String name) {
        for (String supportSuffix : getSupportSuffixes()) {
            if (name.endsWith(supportSuffix)) {
                return true;
            }
        }
        return false;
    }
}
