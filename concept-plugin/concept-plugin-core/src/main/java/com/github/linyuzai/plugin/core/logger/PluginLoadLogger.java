package com.github.linyuzai.plugin.core.logger;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginLifecycleListener;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件加载日志
 */
@Getter
@RequiredArgsConstructor
public class PluginLoadLogger implements PluginLifecycleListener {

    private final PluginLogger logger;

    @Override
    public void onCreate(Plugin plugin) {

    }

    @Override
    public void onPrepare(Plugin plugin) {

    }

    @Override
    public void onLoaded(Plugin plugin) {
        logger.info("Load " + plugin);
    }

    @Override
    public void onUnloaded(Plugin plugin) {
        logger.info("Unload " + plugin);
    }
}
