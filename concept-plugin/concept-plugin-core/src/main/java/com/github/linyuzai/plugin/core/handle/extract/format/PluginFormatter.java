package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件格式器
 */
public interface PluginFormatter {

    /**
     * 格式化
     */
    Object format(Object source, PluginContext context);
}
