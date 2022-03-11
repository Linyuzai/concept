package com.github.linyuzai.plugin.core.exception;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;

@Getter
public class PluginConflictException extends PluginException {

    private final Plugin plugin;

    private final Plugin newPlugin;

    public PluginConflictException(Plugin plugin, Plugin newPlugin) {
        super("Plugin conflicted: " + plugin.getId());
        this.plugin = plugin;
        this.newPlugin = newPlugin;
    }
}
