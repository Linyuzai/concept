package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件工厂抽象类
 */
public abstract class AbstractPluginFactory<T> implements PluginFactory {

    @Override
    public Plugin create(Object source, PluginContext context, PluginConcept concept) {
        //转成支持解析的对象
        T supported = getSupported(source);
        if (supported == null) {
            return null;
        }
        //创建插件配置
        PluginMetadata metadata = createMetadata(supported);
        if (metadata == null) {
            return null;
        }
        //创建插件
        Plugin plugin = doCreate(supported, metadata, context, concept);
        if (plugin == null) {
            return null;
        }
        plugin.setSource(source);
        plugin.setMetadata(metadata);
        return plugin;
    }

    protected PluginMetadata createMetadata(Object source) {
        return getMetadataFactory().create(source);
    }

    protected abstract T getSupported(Object source);

    protected abstract Plugin doCreate(T supported, PluginMetadata metadata, PluginContext context, PluginConcept concept);
}
