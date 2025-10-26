package com.github.linyuzai.plugin.core.path;

/**
 * 插件路径工厂
 */
public interface PluginPathFactory {

    /**
     * 创建插件路径
     *
     * @param parent 插件父路径
     * @param name   插件项名称
     * @return 插件路径
     */
    String create(String parent, String name);
}
