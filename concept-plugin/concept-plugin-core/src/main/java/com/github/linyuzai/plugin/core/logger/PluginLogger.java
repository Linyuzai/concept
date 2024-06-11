package com.github.linyuzai.plugin.core.logger;

public interface PluginLogger {

    String TAG = "Plugin >> ";

    void info(String message);

    void error(String message, Throwable e);
}
