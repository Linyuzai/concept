package com.github.linyuzai.plugin.zip.metadata;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.AbstractPluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFilePluginMetadataFactory extends AbstractPluginMetadataFactory {

    @SneakyThrows
    @Override
    public PluginMetadata create(PluginDefinition definition, PluginContext context) {
        try (ZipFile zipFile = getZipFile(definition)) {
            if (zipFile == null) {
                return null;
            }
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                Adapter adapter = getAdapter(entry.getName());
                if (adapter != null) {
                    return adapter.adapt(zipFile.getInputStream(entry));
                }
            }
            return new PropertiesMetadata(new Properties());
        }
    }

    protected ZipFile getZipFile(PluginDefinition definition) throws IOException {
        File file = ZipUtils.getFile(definition, getSupportSuffixes());
        if (file == null) {
            return null;
        }
        return new ZipFile(file);
    }

    protected String[] getSupportSuffixes() {
        return new String[]{ZipPlugin.SUFFIX_ZIP};
    }
}
