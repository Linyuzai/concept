package com.github.linyuzai.plugin.core.exception;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

@Getter
public class PluginLoadException extends PluginException {

    private final PluginContext context;

    public PluginLoadException(PluginContext context, Throwable cause) {
        super(cause);
        this.context = context;
    }
}
