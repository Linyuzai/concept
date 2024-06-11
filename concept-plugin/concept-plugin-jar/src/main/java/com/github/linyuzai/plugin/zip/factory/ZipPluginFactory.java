package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.MetadataPluginFactory;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipPluginFactory extends MetadataPluginFactory<File> {

    @SneakyThrows
    @Override
    public Plugin doCreate(File file, Plugin.Metadata metadata, PluginContext context) {
        ZipInputStream zis = new ZipInputStream(Files.newInputStream(file.toPath()));
        URL url = getURL(file);
        return createZipPlugin(zis, url, new RootFileEntry(url, file));
    }

    protected ZipPlugin createZipPlugin(ZipInputStream zis, URL url, Plugin.Entry parent) {
        return new ZipPlugin(zis, url, parent);
    }

    @Override
    protected Plugin.Metadata createMetadata(File file, PluginContext context) {
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipEntry entry = zipFile.getEntry("plugin.properties");
            if (entry == null) {
                return Plugin.Metadata.EMPTY;
            }
            try (InputStream is = zipFile.getInputStream(entry)) {
                Properties properties = new Properties();
                properties.load(is);
                return new PropertiesMetadata(properties);
            }
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

    @Getter
    @RequiredArgsConstructor
    public static class RootFileEntry implements Plugin.Entry {

        private final Object id;

        private final File file;

        @Override
        public String getName() {
            return "";
        }

        @Override
        public Plugin getPlugin() {
            return null;
        }

        @Override
        public Plugin.Content getContent() {
            return new RootContent(file);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class RootContent implements Plugin.Content {

        private final File file;

        @Override
        public InputStream getInputStream() throws IOException {
            return Files.newInputStream(file.toPath());
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class PropertiesMetadata implements Plugin.Metadata {

        private final Properties properties;

        @Override
        public String get(String key) {
            return properties.getProperty(key);
        }

        @Override
        public String get(String key, String defaultValue) {
            return properties.getProperty(key, defaultValue);
        }

        @Override
        public boolean isEmpty() {
            return properties.isEmpty();
        }
    }
}
