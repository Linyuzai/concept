package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.AbstractPluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.zip.ZipFile;

/**
 * zip文件插件工厂
 */
@Getter
@Setter
public class ZipFilePluginFactory extends AbstractPluginFactory<ZipFile> {

    @SneakyThrows
    @Override
    protected Plugin doCreate(ZipFile source, PluginMetadata metadata, PluginContext context) {
        return new ZipFilePlugin(source, new URL(source.getName()));
    }

    @Override
    protected ZipFile parseSource(Object source, PluginMetadata metadata, PluginContext context) {
        File file = getFile(source, ".zip");
        if (file == null) {
            return null;
        }
        try {
            return new ZipFile(file);
        } catch (Throwable ignore) {
        }
        return null;
    }

    protected File getFile(Object o, String suffix) {
        File file;
        if (o instanceof File) {
            file = (File) o;
        } else if (o instanceof String) {
            file = new File((String) o);
        } else {
            file = null;
        }
        if (file != null &&
                file.exists() &&
                file.isFile() &&
                file.getName().endsWith(suffix)) {
            return file;
        }
        return null;
    }
}
