package com.github.linyuzai.plugin.core.event;

/**
 * 插件异常事件
 */
public interface PluginErrorEvent {

    Throwable getError();
}
