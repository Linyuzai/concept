package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.zip.ZipFile;

/**
 * zip文件插件工厂
 */
public class ZipFilePluginFactory implements PluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        ZipFile zipFile = ZipUtils.getZipFile(source);
        if(zipFile == null) {
            return null;
        }
        URL url = new URL(zipFile.getName());
        return new ZipFilePlugin(zipFile, url);
    }
}
