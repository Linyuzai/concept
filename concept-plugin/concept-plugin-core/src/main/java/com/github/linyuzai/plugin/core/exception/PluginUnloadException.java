package com.github.linyuzai.plugin.core.exception;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import lombok.Getter;

/**
 * 插件卸载异常类
 */
@Getter
public class PluginUnloadException extends PluginException {

    private final PluginDefinition definition;

    public PluginUnloadException(PluginDefinition definition, Throwable cause) {
        super(cause);
        this.definition = definition;
    }
}
