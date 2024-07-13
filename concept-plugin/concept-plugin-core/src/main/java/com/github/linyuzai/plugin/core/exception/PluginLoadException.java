package com.github.linyuzai.plugin.core.exception;

import lombok.Getter;

/**
 * 插件加载异常类
 */
@Getter
public class PluginLoadException extends PluginException {

    private final Object source;

    public PluginLoadException(Object source, Throwable cause) {
        super(cause);
        this.source = source;
    }
}
