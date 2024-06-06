package com.github.linyuzai.plugin.core.read.metadata;

import com.github.linyuzai.plugin.core.read.PluginReadable;

public interface PluginMetadata extends PluginReadable {

    String get(String key);
}
