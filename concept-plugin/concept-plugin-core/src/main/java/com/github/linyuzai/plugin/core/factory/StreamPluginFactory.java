package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * 流插件工厂
 */
public abstract class StreamPluginFactory extends AbstractPluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context) {
        if (support(definition)) {
            return create(definition::getInputStream);
        }
        return null;
    }

    /**
     * 根据输入流创建插件
     *
     * @param supplier 输入流
     * @return 插件
     */
    protected abstract Plugin create(Supplier<InputStream> supplier);

    /**
     * 根据插件定义是否支持创建插件
     *
     * @param definition 插件定义
     * @return 是否支持创建插件
     */
    protected abstract boolean support(PluginDefinition definition);
}
