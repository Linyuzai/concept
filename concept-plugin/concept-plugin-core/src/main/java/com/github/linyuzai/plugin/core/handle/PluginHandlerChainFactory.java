package com.github.linyuzai.plugin.core.handle;

import java.util.Collection;

public interface PluginHandlerChainFactory {

    PluginHandlerChain create(Collection<? extends PluginHandler> handlers);
}
