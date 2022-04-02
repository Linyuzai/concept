package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件解析链
 */
public interface PluginResolverChain {

    /**
     * 进一步解析
     *
     * @param context 上下文 {@link PluginContext}
     */
    void next(PluginContext context);
}
