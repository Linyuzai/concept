package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件转换器。
 * 如可将 {@link String} 类型的 json 数据转换为 {@link java.io.InputStream} 以适配指定的类型
 */
public interface PluginConvertor {

    /**
     * 转换
     *
     * @param source 转换前数据
     * @return 转换后的数据
     */
    Object convert(Object source, PluginContext context);
}
