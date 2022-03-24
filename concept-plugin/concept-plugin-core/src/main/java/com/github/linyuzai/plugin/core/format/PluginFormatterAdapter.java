package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.util.TypeMetadata;

public interface PluginFormatterAdapter {

    PluginFormatter adapt(TypeMetadata metadata);
}
