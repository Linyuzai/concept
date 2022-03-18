package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginResolverChain {

    void next(PluginContext context);
}
