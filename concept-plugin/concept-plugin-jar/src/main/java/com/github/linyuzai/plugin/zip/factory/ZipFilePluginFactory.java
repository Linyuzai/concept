package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.zip.ZipFile;

/**
 * zip文件插件工厂
 */
public class ZipFilePluginFactory implements PluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context) {
        File file = ZipUtils.getFile(definition, ZipPlugin.SUFFIX_ZIP);
        if(file == null) {
            return null;
        }
        URL url = new URL(file.getAbsolutePath());
        return new ZipFilePlugin(new ZipFile(file), url);
    }
}
