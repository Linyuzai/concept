package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.PluginResolverDependency;

import java.lang.reflect.Type;

public interface PluginMatcher extends PluginResolverDependency {

    Object match(PluginContext context);
}
