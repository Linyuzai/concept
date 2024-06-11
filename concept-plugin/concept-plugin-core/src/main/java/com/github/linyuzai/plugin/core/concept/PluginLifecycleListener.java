package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.*;

public interface PluginLifecycleListener extends PluginEventListener {

    @Override
    default void onEvent(Object event) {
        if (event instanceof PluginCreatedEvent) {
            onCreate(((PluginCreatedEvent) event).getPlugin());
        } else if (event instanceof PluginPreparedEvent) {
            onPrepare(((PluginPreparedEvent) event).getContext());
        } else if (event instanceof PluginReleasedEvent) {
            onRelease(((PluginReleasedEvent) event).getContext());
        } else if (event instanceof PluginLoadedEvent) {
            onLoaded(((PluginLoadedEvent) event).getPlugin());
        } else if (event instanceof PluginUnloadedEvent) {
            onUnloaded(((PluginUnloadedEvent) event).getPlugin());
        }
    }

    void onCreate(Plugin plugin);

    void onPrepare(PluginContext context);

    void onRelease(PluginContext context);

    void onLoaded(Plugin plugin);

    void onUnloaded(Plugin plugin);
}
