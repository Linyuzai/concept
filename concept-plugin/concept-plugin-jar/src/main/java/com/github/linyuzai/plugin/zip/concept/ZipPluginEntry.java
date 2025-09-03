package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * zip插件条目
 */
public interface ZipPluginEntry extends Plugin.Entry {

    default boolean isDirectory() {
        return getName().endsWith("/");
    }
}
