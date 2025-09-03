package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 插件实例匹配器
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PluginObjectMatcher implements PluginMatcher {

    /**
     * 插件类
     */
    private Class<?> pluginClass = Plugin.class;

    @Override
    public Object match(PluginContext context) {
        Plugin plugin = context.getPlugin();
        if (pluginClass.isInstance(plugin)) {
            return plugin;
        }
        return null;
    }
}
