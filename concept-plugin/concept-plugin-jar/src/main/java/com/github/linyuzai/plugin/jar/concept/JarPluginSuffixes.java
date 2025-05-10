package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

public interface JarPluginSuffixes {

    String[] SUPPORTS_SUFFIXES = new String[]{JarPlugin.SUFFIX_JAR, ZipPlugin.SUFFIX_ZIP};

    default String[] getSupportedSuffixes() {
        return SUPPORTS_SUFFIXES;
    }
}
