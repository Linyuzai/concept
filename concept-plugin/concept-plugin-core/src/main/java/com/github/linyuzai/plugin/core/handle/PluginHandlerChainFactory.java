package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.util.Collection;

/**
 * 插件处理链工厂
 */
public interface PluginHandlerChainFactory {

    /**
     * 创建插件处理链
     */
    PluginHandlerChain create(Collection<? extends PluginHandler> handlers, PluginContext context, PluginConcept concept);
}
