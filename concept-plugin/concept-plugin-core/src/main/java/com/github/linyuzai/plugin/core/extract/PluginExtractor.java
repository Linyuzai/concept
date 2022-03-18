package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.PluginResolverDependency;

public interface PluginExtractor extends PluginResolverDependency {

    void extract(PluginContext context);
}
