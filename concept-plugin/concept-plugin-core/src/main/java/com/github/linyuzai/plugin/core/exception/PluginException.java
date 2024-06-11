package com.github.linyuzai.plugin.core.exception;

/**
 * 插件异常类
 */
public class PluginException extends RuntimeException {

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    protected PluginException(Throwable cause) {
        super(cause);
    }
}
