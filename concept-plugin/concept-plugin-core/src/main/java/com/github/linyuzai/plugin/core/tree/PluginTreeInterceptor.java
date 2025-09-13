package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginTreeInterceptor {

    void intercept(PluginDefinition definition, PluginContext context);
}
