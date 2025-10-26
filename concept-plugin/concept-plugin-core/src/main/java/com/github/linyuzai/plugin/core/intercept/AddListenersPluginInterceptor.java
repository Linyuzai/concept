package com.github.linyuzai.plugin.core.intercept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.listener.PluginListener;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 添加插件监听器的拦截器
 */
@RequiredArgsConstructor
public class AddListenersPluginInterceptor implements PluginInterceptor {

    private final Collection<PluginListener> listeners;

    @Override
    public void afterCreatePlugin(Plugin plugin, PluginDefinition definition, PluginContext context) {
        listeners.forEach(plugin::addListener);
    }
}
