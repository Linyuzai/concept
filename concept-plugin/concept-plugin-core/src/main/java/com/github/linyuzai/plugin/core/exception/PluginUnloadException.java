package com.github.linyuzai.plugin.core.exception;

import lombok.Getter;

@Getter
public class PluginUnloadException extends PluginException {

    private final Object plugin;

    public PluginUnloadException(Object plugin, Throwable cause) {
        super(cause);
        this.plugin = plugin;
    }
}
