package com.github.linyuzai.plugin.core.logger;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.concept.PluginLifecycleListener;
import com.github.linyuzai.plugin.core.exception.PluginLoadException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件标准日志
 */
@Getter
@RequiredArgsConstructor
public class PluginStandardLogger implements PluginLifecycleListener {

    private final PluginLogger logger;

    @Override
    public void onLoaded(Plugin plugin) {
        logger.info("Loaded " + plugin);
    }

    @Override
    public void onUnloaded(Plugin plugin) {
        logger.info("Unloaded " + plugin);
    }

    @Override
    public void onError(PluginDefinition definition, Throwable e) {
        String message = "Load error: " + definition.getPath();
        logger.error(message, getPluginException(e));
    }

    private Throwable getPluginException(Throwable e) {
        if (e instanceof PluginLoadException) {
            return getPluginException(e.getCause());
        }
        return e;
    }
}
