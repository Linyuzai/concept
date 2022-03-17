package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.PluginResolverDependency;

public interface PluginMatcher extends PluginResolverDependency {

    boolean isMatched(PluginContext context);

    Object getMatched(PluginContext context);

    void match(PluginContext context);
}
