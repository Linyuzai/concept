package com.github.linyuzai.plugin.core.exception;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;

@Getter
public class PluginConflictException extends PluginException {

    private final Plugin curPlugin;

    private final Plugin newPlugin;

    public PluginConflictException(String id, Plugin curPlugin, Plugin newPlugin) {
        super("Plugin conflicted: " + id);
        this.curPlugin = curPlugin;
        this.newPlugin = newPlugin;
    }
}
