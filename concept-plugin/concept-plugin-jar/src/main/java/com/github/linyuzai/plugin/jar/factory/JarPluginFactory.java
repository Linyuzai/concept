package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

public interface JarPluginFactory extends PluginFactory {

    default String[] getSupportedSuffixes() {
        return new String[]{JarPlugin.SUFFIX_JAR, ZipPlugin.SUFFIX_ZIP};
    }
}
