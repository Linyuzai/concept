package com.github.linyuzai.plugin.core.exception;

import lombok.Getter;

@Getter
public class PluginLoadException extends PluginException {

    private final Object source;

    public PluginLoadException(Object source, Throwable cause) {
        super(cause);
        this.source = source;
    }
}
