package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginResolver extends PluginResolverDependency {

    void resolve(PluginContext context);

    boolean support(PluginContext context);
}
