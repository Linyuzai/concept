package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.concept.Plugin;

public interface PluginMatcher {

    boolean match(Plugin plugin, Object resolved);
}
