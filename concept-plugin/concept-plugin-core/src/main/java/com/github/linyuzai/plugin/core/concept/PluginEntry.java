package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.read.content.PluginContent;

public interface PluginEntry {

    String getName();

    Plugin getPlugin();

    PluginContent getContent();
}
