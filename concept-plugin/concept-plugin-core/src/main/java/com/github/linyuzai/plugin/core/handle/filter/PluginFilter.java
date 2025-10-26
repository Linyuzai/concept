package com.github.linyuzai.plugin.core.handle.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandler;

/**
 * 插件过滤器
 */
public interface PluginFilter extends PluginHandler, PluginHandler.Dependency {

    @Override
    default void handle(PluginContext context) {
        filter(context);
    }

    /**
     * 过滤
     */
    void filter(PluginContext context);

    /**
     * 取反
     */
    PluginFilter negate();

    /**
     * 排序值
     */
    default int getOrder() {
        return 0;
    }
}
