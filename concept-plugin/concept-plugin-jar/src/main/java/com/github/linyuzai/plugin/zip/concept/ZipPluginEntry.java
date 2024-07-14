package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.net.URL;

/**
 * zip插件条目
 */
public interface ZipPluginEntry extends Plugin.Entry {

    URL getURL();

    default boolean isDirectory() {
        return getName().endsWith("/");
    }
}
