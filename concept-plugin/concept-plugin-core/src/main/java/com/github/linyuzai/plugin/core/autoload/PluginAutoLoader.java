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
     * 开始监听
     *
     * @param load 加载已经存在的插件
     */
    void start(boolean load);

    /**
     * 停止监听
     */
    void stop();

    /**
     * 添加分组监听
     */
    void addGroup(String group);

    /**
     * 获取分组监听状态
     */
    Boolean getGroupState(String group);
}
