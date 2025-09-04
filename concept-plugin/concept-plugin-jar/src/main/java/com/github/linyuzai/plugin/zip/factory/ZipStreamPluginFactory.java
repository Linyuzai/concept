package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.StreamPluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip流插件工厂
 */
public class ZipStreamPluginFactory extends StreamPluginFactory {

    protected ZipStreamPlugin create(Supplier<InputStream> supplier) {
        return new ZipStreamPlugin(supplier);
    }

    @SneakyThrows
    @Override
    public PluginMetadata create(PluginDefinition definition, PluginContext context) {
        try (ZipInputStream zis = getZipInputStream(definition)) {
            if (zis == null) {
                return null;
            }
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                String name = zipEntry.getName();
                if (!zipEntry.isDirectory()) {
                    PluginMetadata.Adapter adapter = getMetadataAdapter(name);
                    if (adapter != null) {
                        return adapter.adapt(zis);
                    }
                }
                zis.closeEntry();
            }
        }
        return new PropertiesMetadata(new Properties());
    }

    @Nullable
    protected ZipInputStream getZipInputStream(PluginDefinition definition) {
        InputStream is = definition.getInputStream();
        if (is == null) {
            return null;
        }
        return new ZipInputStream(is);
    }

    @Override
    protected boolean support(PluginDefinition definition) {
        return definition.getPath().endsWith(ZipPlugin.SUFFIX_ZIP);
    }
}
