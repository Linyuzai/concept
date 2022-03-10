package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.PluginResolverDependency;

public interface PluginResolver extends PluginResolverDependency {

    boolean support(PluginContext context);

    void resolve(PluginContext context, PluginResolverChain chain);
}
