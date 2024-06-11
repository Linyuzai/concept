package com.github.linyuzai.plugin.core.autoload;

import java.util.concurrent.Executor;

public interface PluginExecutorProvider {

    PluginExecutorProvider NO_EXECUTOR = () -> null;

    Executor get();
}
