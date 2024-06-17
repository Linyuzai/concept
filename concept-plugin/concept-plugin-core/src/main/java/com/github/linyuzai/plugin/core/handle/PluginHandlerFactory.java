package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginHandlerFactory {

    PluginHandler create(PluginContext context);
}
