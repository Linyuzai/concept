package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;

/**
 * 插件工厂抽象类
 */
public abstract class AbstractPluginFactory<T> implements PluginFactory {

    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        T parse = parseSource(source, metadata, context);
        if (parse == null) {
            return null;
        }
        return doCreate(parse, metadata, context);
    }

    protected abstract Plugin doCreate(T source, PluginMetadata metadata, PluginContext context);

    protected abstract T parseSource(Object source, PluginMetadata metadata, PluginContext context);
}
