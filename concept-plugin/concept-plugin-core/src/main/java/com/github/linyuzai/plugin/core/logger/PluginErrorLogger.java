package com.github.linyuzai.plugin.core.logger;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.event.PluginErrorEvent;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.exception.PluginLoadException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Deprecated
@Getter
@RequiredArgsConstructor
public class PluginErrorLogger implements PluginEventListener {

    private final PluginLogger logger;

    @Override
    public void onEvent(Object event) {
        if (event instanceof PluginErrorEvent) {
            Throwable error = ((PluginErrorEvent) event).getError();
            if (error instanceof PluginLoadException) {
                PluginDefinition definition = ((PluginLoadException) error).getDefinition();
                String message = definition == null ? "Load error" : "Load error: " + definition.getPath();
                logger.error(message, getPluginException(error));
            } else {
                logger.error("Plugin error", error);
            }
        }
    }

    private Throwable getPluginException(Throwable e) {
        if (e instanceof PluginLoadException) {
            return getPluginException(e.getCause());
        }
        return e;
    }
}
