package com.github.linyuzai.plugin.core.autoload;

/**
 * 插件自动加载
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

    /**
     * 刷新
     */
    void refresh();
}
