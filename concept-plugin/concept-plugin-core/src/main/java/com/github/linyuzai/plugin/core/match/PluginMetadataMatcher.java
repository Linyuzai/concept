package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件 {@link Plugin} 匹配器
 */
@Getter
@RequiredArgsConstructor
public class PluginMetadataMatcher implements PluginMatcher {

    /**
     * 插件类
     */
    private final Class<?> metadataClass;

    public PluginMetadataMatcher() {
        this(Plugin.Metadata.class);
    }

    /**
     * 通过 {@link Class#isInstance(Object)} 匹配
     *
     * @param context 上下文 {@link PluginContext}
     * @return 匹配到的插件 {@link Plugin}
     */
    @Override
    public Object match(PluginContext context) {
        Plugin.Metadata metadata = context.getPlugin().getMetadata();
        if (metadataClass.isInstance(metadata)) {
            return metadata;
        }
        return null;
    }
}
