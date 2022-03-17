package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.PluginResolverDependency;

import java.lang.reflect.Type;

public interface PluginMatcher extends PluginResolverDependency {

    boolean isMatched(PluginContext context);

    Object getMatched(PluginContext context);
}
