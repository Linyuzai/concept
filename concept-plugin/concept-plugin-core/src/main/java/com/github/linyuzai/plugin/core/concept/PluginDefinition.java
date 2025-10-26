package com.github.linyuzai.plugin.core.concept;

import java.io.InputStream;

/**
 * 插件定义
 */
public interface PluginDefinition {

    /**
     * 插件路径，唯一
     */
    String getPath();

    /**
     * 插件名
     */
    String getName();

    /**
     * 插件大小
     */
    long getSize();

    /**
     * 创建时间
     */
    long getCreateTime();

    /**
     * 插件版本
     */
    Object getVersion();

    /**
     * 获得插件输入流
     */
    InputStream getInputStream();
}
