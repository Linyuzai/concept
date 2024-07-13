package com.github.linyuzai.plugin.core.metadata;

/**
 * 插件配置工厂
 */
public interface PluginMetadataFactory {

    /**
     * 创建插件配置
     */
    PluginMetadata create(Object source);
}
