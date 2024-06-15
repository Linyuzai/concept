package com.github.linyuzai.plugin.core.lock;

import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;

@Getter
public class PluginLockException extends PluginException {

    private final Object plugin;

    private final Object current;

    private final Object request;

    protected PluginLockException(Object plugin, Object current, Object request) {
        super("Plugin lock failure: " + plugin);
        this.plugin = plugin;
        this.current = current;
        this.request = request;
    }
}
