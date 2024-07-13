package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.util.Collection;

/**
 * 默认的插件处理链工厂
 */
public class DefaultPluginHandlerChainFactory implements PluginHandlerChainFactory {

    @Override
    public PluginHandlerChain create(Collection<? extends PluginHandler> handlers, PluginContext context, PluginConcept concept) {
        return new DefaultPluginHandlerChain(handlers);
    }
}
