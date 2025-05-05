package com.github.linyuzai.plugin.zip.metadata;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFinder;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.util.ZipUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFilePluginMetadataFinder implements PluginMetadataFinder {

    @Override
    public PluginMetadata find(Object source, PluginContext context) {
        File file = ZipUtils.getFile(source, ZipPlugin.SUFFIX);
        if (file == null) {
            return null;
        }
        try (ZipFile zipFile = new ZipFile(file)) {
            Properties properties = new Properties();
            ZipEntry entry = zipFile.getEntry(PluginMetadata.NAME);
            if (entry != null) {
                try (InputStream is = zipFile.getInputStream(entry)) {
                    properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
                }
            }
            return new PropertiesMetadata(properties);
        } catch (Throwable e) {
            return null;
        }
    }
}
