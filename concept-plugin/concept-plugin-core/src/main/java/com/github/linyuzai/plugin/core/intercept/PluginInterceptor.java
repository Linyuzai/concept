package com.github.linyuzai.plugin.core.intercept;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginInterceptor {

    void beforeCreate(PluginDefinition definition, PluginContext context);
}
