package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * zip流插件工厂
 */
public class ZipStreamPluginFactory implements PluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context) {
        if (definition instanceof PluginDefinition.Loadable) {
            PluginDefinition.Loadable loadable = (PluginDefinition.Loadable) definition;
            return create(loadable::getInputStream);
        } else if (definition instanceof Plugin.Entry) {
            Plugin.Entry entry = (Plugin.Entry) definition;
            if (support(entry.getName())) {
                return create(() -> entry.getContent().getInputStream());
            }
        }
        return null;
    }

    protected ZipStreamPlugin create(Supplier<InputStream> supplier) {
        return new ZipStreamPlugin(supplier);
    }

    protected boolean support(String name) {
        return name.endsWith(ZipPlugin.SUFFIX_ZIP);
    }
}
