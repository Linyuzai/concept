package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

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

    /**
     * 根据插件文件创建插件
     *
     * @param file 插件文件
     * @return 插件
     */
    protected abstract Plugin create(File file);

    /**
     * 根据插件定义是否支持创建插件
     *
     * @param definition 插件定义
     * @return 是否支持创建插件
     */
    protected abstract boolean support(PluginDefinition definition);

    /**
     * 根据插件定义获得插件文件
     *
     * @param definition 插件定义
     * @return 插件文件
     */
    @Nullable
    public static File getFile(PluginDefinition definition) {
        File file = new File(definition.getPath());
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }
}
