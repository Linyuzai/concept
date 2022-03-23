package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.util.TypeMetadata;

public interface PluginConvertorAdapter {

    PluginConvertor adapt(TypeMetadata metadata);
}
