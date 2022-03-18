package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.PluginResolverDependency;

public interface PluginMatcher extends PluginResolverDependency {

    Object match(PluginContext context);
}
