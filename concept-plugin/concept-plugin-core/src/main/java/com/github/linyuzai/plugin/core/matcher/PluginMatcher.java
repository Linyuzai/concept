package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.PluginResolverDependency;

public interface PluginMatcher extends PluginResolverDependency {

    void match(PluginContext context);
}
