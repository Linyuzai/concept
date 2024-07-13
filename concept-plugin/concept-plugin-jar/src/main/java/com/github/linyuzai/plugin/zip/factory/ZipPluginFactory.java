package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.AbstractPluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Getter
@Setter
public class ZipPluginFactory extends AbstractPluginFactory<File> {

    private PluginMetadataFactory metadataFactory = new ZipPluginMetadataFactory();

    @SneakyThrows
    @Override
    protected Plugin doCreate(File file, PluginMetadata metadata, PluginContext context, PluginConcept concept) {
        return createZipPlugin(file, getURL(file));
    }

    protected ZipFilePlugin createZipPlugin(File file, URL url) {
        return new ZipFilePlugin(file.getAbsolutePath(), url);
    }

    @Override
    protected File getSupported(Object source) {
        File file = getFile(source);
        if (file != null && supportFile(file)) {
            return file;
        }
        return null;
    }

    protected boolean supportFile(File file) {
        try {
            ZipFile zf = new ZipFile(file);
            zf.close();
            return true;
        } catch (Throwable ignore) {
        }
        return false;
    }

    protected URL getURL(File file) throws MalformedURLException {
        return file.toURI().toURL();
    }

    public static File getFile(Object o) {
        File file;
        if (o instanceof File) {
            file = (File) o;
        } else if (o instanceof String) {
            file = new File((String) o);
        } else {
            file = null;
        }
        if (file != null && file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    public class ZipPluginMetadataFactory implements PluginMetadataFactory {

        @Override
        public PluginMetadata create(Object source) {
            File file = getSupported(source);
            if (file == null) {
                return null;
            }
            try (ZipFile zipFile = new ZipFile(file)) {
                Properties properties = new Properties();
                ZipEntry entry = zipFile.getEntry(PluginMetadata.NAME);
                if (entry != null) {
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        properties.load(is);
                    }
                }
                return new PropertiesMetadata(properties);
            } catch (Throwable e) {
                return null;
            }
        }
    }
}
