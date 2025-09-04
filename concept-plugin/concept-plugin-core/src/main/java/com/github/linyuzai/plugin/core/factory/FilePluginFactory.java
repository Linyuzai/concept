package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.SneakyThrows;

import java.io.File;

/**
 * 文件插件工厂
 */
public abstract class FilePluginFactory extends AbstractPluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context) {
        File file = getFile(definition);
        if (file == null) {
            return null;
        }
        return create(file);
    }

    protected abstract Plugin create(File file);

    protected abstract boolean support(PluginDefinition definition);

    public static File getFile(PluginDefinition definition) {
        File file = new File(definition.getPath());
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }
}
