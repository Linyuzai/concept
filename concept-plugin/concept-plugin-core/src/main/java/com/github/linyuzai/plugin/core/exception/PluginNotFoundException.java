package com.github.linyuzai.plugin.core.exception;

public class PluginNotFoundException extends PluginException {

    public PluginNotFoundException(String id) {
        super("Plugin not found: " + id);
    }
}
