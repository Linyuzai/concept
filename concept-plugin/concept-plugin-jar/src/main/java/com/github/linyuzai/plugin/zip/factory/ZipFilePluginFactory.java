package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.FilePluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip文件插件工厂
 */
public class ZipFilePluginFactory extends FilePluginFactory {

    @Override
    protected Plugin create(File file) {
        return new ZipFilePlugin(getZipFile(file));
    }

    @SneakyThrows
    @Override
    public PluginMetadata create(PluginDefinition definition, PluginContext context) {
        try (ZipFile zipFile = getZipFile(getFile(definition))) {
            if (zipFile == null) {
                return null;
            }
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                PluginMetadata.Adapter adapter = getMetadataAdapter(entry.getName());
                if (adapter != null) {
                    return adapter.adapt(zipFile.getInputStream(entry));
                }
            }
            return new PropertiesMetadata(new Properties());
        }
    }

    @SneakyThrows
    protected ZipFile getZipFile(File file) {
        return new ZipFile(file);
    }

    @Override
    protected boolean support(PluginDefinition definition) {
        return definition.getPath().endsWith(ZipPlugin.SUFFIX_ZIP);
    }
}
