package com.github.linyuzai.plugin.autoconfigure.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.Getter;
import lombok.Setter;

/**
 * 可绑定配置文件的插件工厂
 */
@Getter
@Setter
public class BinderPluginFactory extends BinderPluginMetadataFactory implements PluginFactory {

    private PluginFactory pluginFactory;

    @Override
    public Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context) {
        return pluginFactory.create(definition, metadata, context);
    }
}
