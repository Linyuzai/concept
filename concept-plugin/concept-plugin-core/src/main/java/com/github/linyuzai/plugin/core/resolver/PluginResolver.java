package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.PluginResolverDependency;

public interface PluginResolver extends PluginResolverDependency {

    void resolve(PluginContext context);

    boolean support(PluginContext context);
}
