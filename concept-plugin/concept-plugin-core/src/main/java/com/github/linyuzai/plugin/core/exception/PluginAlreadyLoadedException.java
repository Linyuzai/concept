package com.github.linyuzai.plugin.core.exception;

public class PluginAlreadyLoadedException extends PluginException {

    public PluginAlreadyLoadedException(String id) {
        super("Plugin is already loaded: " + id);
    }
}
