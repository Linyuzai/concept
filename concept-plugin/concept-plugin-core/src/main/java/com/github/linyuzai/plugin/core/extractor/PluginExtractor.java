package com.github.linyuzai.plugin.core.extractor;

import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginExtractor {

    boolean support(PluginContext context);

    void extract(PluginContext context);
}
