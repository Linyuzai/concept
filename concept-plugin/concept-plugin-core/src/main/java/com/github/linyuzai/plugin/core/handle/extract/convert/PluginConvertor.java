package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件转换器
 */
public interface PluginConvertor {

    /**
     * 转换
     */
    Object convert(Object source, PluginContext context);
}
