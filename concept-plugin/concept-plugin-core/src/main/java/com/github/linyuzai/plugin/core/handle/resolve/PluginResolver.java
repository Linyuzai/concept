package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件解析器
 */
public interface PluginResolver extends PluginHandler, PluginHandler.Dependency {

    /**
     * 解析
     *
     * @param context 上下文 {@link PluginContext}
     */
    void resolve(PluginContext context);
}
