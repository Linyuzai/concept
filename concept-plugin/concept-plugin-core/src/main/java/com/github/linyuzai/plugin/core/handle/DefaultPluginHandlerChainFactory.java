package com.github.linyuzai.plugin.core.handle;

import java.util.Collection;

public class DefaultPluginHandlerChainFactory implements PluginHandlerChainFactory {

    @Override
    public PluginHandlerChain create(Collection<? extends PluginHandler> handlers) {
        return new DefaultPluginHandlerChain(handlers);
    }
}
