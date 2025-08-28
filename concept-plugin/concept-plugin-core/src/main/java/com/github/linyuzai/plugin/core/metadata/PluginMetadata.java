package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.Set;

/**
 * 插件配置
 */
public interface PluginMetadata {

    /**
     * 配置前缀
     */
    String PREFIX = "concept.plugin";

    /**
     * 配置文件名
     */
    String PROP_NAME = "plugin.properties";

    /**
     * 配置文件名
     */
    String YAML_NAME = "plugin.yaml";

    /**
     * 配置文件名
     */
    String YML_NAME = "plugin.yml";

    /**
     * 获得配置
     */
    String get(String name);

    /**
     * 获得配置（可指定默认值）
     */
    String get(String name, String defaultValue);

    /**
     * 获得所有配置名
     */
    Set<String> names();

    /**
     * 绑定配置
     */
    <T> T bind(String name, Class<T> type);

    /**
     * 获得标准配置
     */
    <T extends Plugin.StandardMetadata> T asStandard();

    /**
     * 设置配置
     */
    void set(String name, String value);

    /**
     * 刷新配置
     */
    void refresh();


}
