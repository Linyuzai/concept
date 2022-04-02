package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.PluginResolverDependency;

/**
 * 插件匹配器
 */
public interface PluginMatcher extends PluginResolverDependency {

    /**
     * 匹配插件
     *
     * @param context 上下文 {@link PluginContext}
     * @return 如果匹配到则返回匹配到的插件，否则返回 null
     */
    Object match(PluginContext context);
}
