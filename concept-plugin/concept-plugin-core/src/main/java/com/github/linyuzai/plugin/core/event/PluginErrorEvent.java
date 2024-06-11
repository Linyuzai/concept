package com.github.linyuzai.plugin.core.event;

/**
 * 插件自动加载异常事件
 */
public interface PluginErrorEvent {

    Throwable getError();
}
