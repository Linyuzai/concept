package com.github.linyuzai.plugin.core.extractor;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.PluginResolverDependency;

public interface PluginExtractor extends PluginResolverDependency {

    void extract(PluginContext context);
}
