package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandler;

/**
 * 插件匹配器
 */
public interface PluginMatcher extends PluginHandler.Dependency {

    /**
     * 匹配
     */
    Object match(PluginContext context);
}
