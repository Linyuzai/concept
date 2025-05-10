package com.github.linyuzai.plugin.zip.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.factory.PluginSourceProvider;
import com.github.linyuzai.plugin.core.metadata.AbstractPluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

public abstract class ZipStreamPluginMetadataFactory extends AbstractPluginMetadataFactory {

    @SneakyThrows
    @Override
    public PluginMetadata create(Object source, PluginContext context) {
        try (ZipInputStream zis = getZipInputStream(source)) {
            if (zis == null) {
                return null;
            }
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

    protected ZipInputStream getZipInputStream(Object source) throws IOException {
        if (source instanceof PluginSourceProvider) {
            PluginSourceProvider provider = (PluginSourceProvider) source;
            return new ZipInputStream(provider.getInputStream());
        }
        return null;
    }
}
