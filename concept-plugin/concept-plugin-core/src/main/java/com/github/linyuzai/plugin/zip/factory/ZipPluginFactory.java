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

    @Override
    public Plugin doCreate(File file, Plugin.Metadata metadata, PluginContext context) {
        String mode = getMode(file, metadata, context);
        return createInMode(file, mode, context);
    }

    public String getMode(File file, Plugin.Metadata metadata, PluginContext context) {
        return metadata.get("concept.plugin.zip.mode", ZipPlugin.Mode.MEMORY);
    }

    @SneakyThrows
    public Plugin createInMode(File file, String mode, PluginContext context) {
        ZipInputStream zis = new ZipInputStream(Files.newInputStream(file.toPath()));
        URL url = getURL(file);
        switch (mode.toUpperCase()) {
            case ZipPlugin.Mode.MEMORY:
                return new ZipPlugin(zis, url, null);
            case ZipPlugin.Mode.FILE:
                return new ZipPlugin(zis, url, new RootFileEntry(url, file));
            default:
                throw new IllegalArgumentException("Mode not supported: " + mode);
        }
    }

    @Override
    protected Plugin.Metadata createMetadata(File file, PluginContext context) {
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipEntry entry = zipFile.getEntry("plugin.properties");
            if (entry == null) {
                return new EmptyMetadata();
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

    public static class EmptyMetadata implements Plugin.Metadata {

        @Override
        public String get(String key) {
            return null;
        }

        @Override
        public String get(String key, String defaultValue) {
            return defaultValue;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
