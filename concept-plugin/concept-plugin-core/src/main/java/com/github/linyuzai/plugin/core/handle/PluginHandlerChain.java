package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件处理链
 */
public interface PluginHandlerChain {

    /**
     * 执行下一个处理器
     */
    void next(PluginContext context);
}
