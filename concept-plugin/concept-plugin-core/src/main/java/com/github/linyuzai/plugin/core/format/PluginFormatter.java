package com.github.linyuzai.plugin.core.format;

/**
 * 插件格式器
 */
public interface PluginFormatter {

    /**
     * 格式化
     *
     * @param source 被格式化的对象
     * @return 格式化后的对象
     */
    Object format(Object source);
}
