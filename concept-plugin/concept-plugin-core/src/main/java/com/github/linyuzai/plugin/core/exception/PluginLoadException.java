package com.github.linyuzai.plugin.core.exception;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import lombok.Getter;

/**
 * 插件加载异常类
 */
@Getter
public class PluginLoadException extends PluginException {

    private final PluginDefinition definition;

    public PluginLoadException(PluginDefinition definition, Throwable cause) {
        super(cause);
        this.definition = definition;
    }
}
