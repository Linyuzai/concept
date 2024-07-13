package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.EmptyMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * 子插件工厂
 * <p>
 * 用于将 {@link Plugin.Entry} 解析成插件
 */
@Getter
@Setter
public abstract class SubPluginFactory extends AbstractPluginFactory<Plugin.Entry> {

    private PluginMetadataFactory metadataFactory = new SubPluginMetadataFactory();

    @Override
    protected Plugin.Entry getSupported(Object source) {
        if (source instanceof Plugin.Entry) {
            return (Plugin.Entry) source;
        }
        return null;
    }

    /**
     * 子插件配置工厂
     * <p>
     * 返回空配置
     */
    public class SubPluginMetadataFactory implements PluginMetadataFactory {

        private final EmptyMetadata metadata = new EmptyMetadata(new Plugin.StandardMetadata());

        @Override
        public PluginMetadata create(Object source) {
            if (getSupported(source) != null) {
                return metadata;
            } else {
                return null;
            }
        }
    }
}
