package com.github.linyuzai.plugin.core.util;

import com.github.linyuzai.plugin.core.autoload.PluginAutoEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoLoadEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoReloadEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoUnloadEvent;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.event.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.function.Consumer;

/**
 * 插件加载日志
 */
@AllArgsConstructor
public class PluginLoadLogger implements PluginEventListener {

    @NonNull
    private final Consumer<String> consumer;

    @Override
    public void onEvent(Object event) {
        if (event instanceof PluginEvent) {
            Plugin plugin = ((PluginEvent) event).getPlugin();
            if (event instanceof PluginAutoEvent) {
                if (event instanceof PluginAutoLoadEvent) {
                    log("Auto load " + plugin);
                } else if (event instanceof PluginAutoReloadEvent) {
                    log("Auto reload " + plugin);
                } else if (event instanceof PluginAutoUnloadEvent) {
                    log("Auto unload " + plugin);
                }
            } else if (event instanceof PluginContextEvent) {

            } else {
                if (event instanceof PluginCreatedEvent) {
                    log("Create " + plugin);
                } else if (event instanceof PluginPreparedEvent) {
                    log("Prepare " + plugin);
                } else if (event instanceof PluginReleasedEvent) {
                    log("Release " + plugin);
                } else if (event instanceof PluginLoadedEvent) {
                    log("Load " + plugin);
                } else if (event instanceof PluginUnloadedEvent) {
                    log("Unload " + plugin);
                }
            }
        }
    }

    public String appendTag(String msg) {
        return "Plugin >> " + msg;
    }

    public void log(String msg) {
        consumer.accept(appendTag(msg));
    }
}
