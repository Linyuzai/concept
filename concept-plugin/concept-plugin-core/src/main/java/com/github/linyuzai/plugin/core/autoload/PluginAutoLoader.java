package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件自动加载，可在某些场景下触发自动加载
 */
public interface PluginAutoLoader {

    /**
     * 开始监听
     */
    void start();

    /**
     * 停止监听
     */
    void stop();

    void addGroup(String group);

    Boolean getGroupState(String group);
}
