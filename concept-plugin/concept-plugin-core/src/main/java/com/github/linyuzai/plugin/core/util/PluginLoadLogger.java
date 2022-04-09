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
    private final Consumer<String> infoLogger;

    private final boolean logDetail;

    public PluginLoadLogger(@NonNull Consumer<String> infoLogger) {
        this(infoLogger, false);
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof PluginEvent) {
            Plugin plugin = ((PluginEvent) event).getPlugin();
            if (event instanceof PluginAutoEvent) {
                if (event instanceof PluginAutoLoadEvent) {
                    info("Auto load " + plugin);
                } else if (event instanceof PluginAutoReloadEvent) {
                    info("Auto reload " + plugin);
                } else if (event instanceof PluginAutoUnloadEvent) {
                    info("Auto unload " + plugin);
                }
            } else if (event instanceof PluginContextEvent) {

            } else {
                if (event instanceof PluginCreatedEvent) {
                    info("Create " + plugin);
                } else if (event instanceof PluginPreparedEvent) {
                    info("Prepare " + plugin);
                } else if (event instanceof PluginReleasedEvent) {
                    info("Release " + plugin);
                } else if (event instanceof PluginLoadedEvent) {
                    info("Load " + plugin);
                } else if (event instanceof PluginUnloadedEvent) {
                    info("Unload " + plugin);
                }
            }
        }
    }

    public String appendTag(String msg) {
        return "Plugin >> " + msg;
    }

    public void info(String msg) {
        infoLogger.accept(appendTag(msg));
    }
}
