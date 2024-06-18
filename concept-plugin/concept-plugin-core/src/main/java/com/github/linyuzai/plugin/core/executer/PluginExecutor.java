package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.TimeUnit;

public interface PluginExecutor {

    void execute(Runnable runnable);

    void execute(Runnable runnable, long delay, TimeUnit unit);
}
