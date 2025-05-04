package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.AbstractPluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip文件插件工厂
 */
@Getter
@Setter
public class ZipPluginFactory extends AbstractPluginFactory<File> {

    private PluginMetadataFactory metadataFactory = new ZipPluginMetadataFactory();

    @SneakyThrows
    @Override
    protected Plugin doCreate(File file, PluginMetadata metadata, PluginContext context) {
        return createPlugin(file, getURL(file));
    }

    /**
     * 创建插件
     */
    protected ZipPlugin createPlugin(File file, URL url) throws IOException {
        return new ZipFilePlugin(new ZipFile(file), url);
    }

    /**
     * 获得文件对象
     */
    @Override
    protected File parseSource(Object source) {
        File file = getFile(source);
        if (file != null && supportFile(file)) {
            return file;
        }
        return null;
    }

    /**
     * 是否支持文件
     */
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

    /**
     * zip插件配置工厂
     */
    public class ZipPluginMetadataFactory implements PluginMetadataFactory {

        @Override
        public PluginMetadata create(Object source) {
            File file = parseSource(source);
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
}
