package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandler;

/**
 * 插件解析器
 */
public interface PluginResolver extends PluginHandler, PluginHandler.Dependency {

    @Override
    default void handle(PluginContext context) {
        resolve(context);
    }

    /**
     * 解析
     */
    void resolve(PluginContext context);
}
