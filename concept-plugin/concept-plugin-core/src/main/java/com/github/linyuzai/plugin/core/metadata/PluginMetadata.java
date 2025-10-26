package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.io.InputStream;
import java.util.Set;

/**
 * 插件元数据
 */
public interface PluginMetadata {

    /**
     * 插件元数据前缀
     */
    String PREFIX = "concept.plugin";

    /**
     * 插件元数据文件名
     */
    String PROP_NAME = "plugin.properties";

    /**
     * 插件元数据文件名
     */
    String YAML_NAME = "plugin.yaml";

    /**
     * 插件元数据文件名
     */
    String YML_NAME = "plugin.yml";

    /**
     * 获得插件元数据
     */
    String get(String name);

    /**
     * 获得插件元数据（可指定默认值）
     */
    String get(String name, String defaultValue);

    /**
     * 获得所有插件元数据名
     */
    Set<String> names();

    /**
     * 绑定插件元数据
     */
    <T> T bind(String name, Class<T> type);

    /**
     * 获得标准插件元数据
     */
    <T extends Plugin.StandardMetadata> T asStandard();

    /**
     * 设置插件元数据
     */
    void set(String name, String value);

    /**
     * 刷新插件元数据
     */
    void refresh();

    /**
     * 插件元数据适配器
     */
    interface Adapter {

        boolean support(String name);

        PluginMetadata adapt(InputStream is);
    }
}
