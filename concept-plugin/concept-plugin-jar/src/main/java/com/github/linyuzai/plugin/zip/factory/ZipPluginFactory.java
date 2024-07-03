package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.MetadataPluginFactory;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipPluginFactory extends MetadataPluginFactory<File> {

    @SneakyThrows
    @Override
    public Plugin doCreate(File file, PluginMetadata metadata, PluginContext context) {
        //ZipInputStream zis = new ZipInputStream(Files.newInputStream(file.toPath()));
        return createZipPlugin(file, getURL(file));
    }

    protected ZipFilePlugin createZipPlugin(File file, URL url) {
        return new ZipFilePlugin(file.getAbsolutePath(), url);
    }

    @Override
    protected PluginMetadata createMetadata(File file, PluginContext context) {
        try {
            return createMetadata(file);
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    protected File getSource(Object o, PluginContext context) {
        File file = getFile(o, context);
        if (file != null && supportFile(file, context)) {
            return file;
        }
        return null;
    }

    protected boolean supportFile(File file, PluginContext context) {
        return file.getName().endsWith(".zip");
    }

    protected File getFile(Object o, PluginContext context) {
        if (o instanceof File) {
            File file = (File) o;
            if (file.exists()) {
                return file;
            }
        } else if (o instanceof String) {
            File file = new File((String) o);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    protected URL getURL(File file) throws MalformedURLException {
        return file.toURI().toURL();
    }

    public static PluginMetadata createMetadata(File file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file)) {
            Properties properties = new Properties();
            ZipEntry entry = zipFile.getEntry(PluginMetadata.NAME);
            if (entry != null) {
                try (InputStream is = zipFile.getInputStream(entry)) {
                    properties.load(is);
                }
            }
            return new PropertiesMetadata(properties);
        }
    }
}
